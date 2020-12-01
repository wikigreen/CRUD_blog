package com.vladimir.crudblog.repository;

import com.vladimir.crudblog.model.Post;
import com.vladimir.crudblog.model.Region;
import com.vladimir.crudblog.model.Role;
import com.vladimir.crudblog.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class  UserRepository implements GenericRepository<User, Long>{
    private static UserRepository instance;
    private final File file;
    DataOutputStream dataOutputStream;
    private Long lastId;
    private final PostRepository postRepository;
    private final RegionRepository regionRepository;

    private UserRepository() throws IOException {
        file = new File("src//main//resources//files//users.txt");
        dataOutputStream = new DataOutputStream(new FileOutputStream(file, true));
        postRepository = PostRepository.getInstance();
        regionRepository = RegionRepository.getInstance();
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            try {
                instance = new UserRepository();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    @Override
    public User save(User user) {
        if (user.getId() == null)
            user.setId(generateId());
        try {
            dataOutputStream.writeLong(user.getId());//writing user ID
            dataOutputStream.writeUTF(user.getFirstName());// writing fn
            dataOutputStream.writeUTF(user.getLastName());// writing ln
            dataOutputStream.writeLong(user.getRegion() == null ? (long)0 : user.getRegion().getId());// writing region ID
            dataOutputStream.writeUTF(user.getRole().toString());// writing role
            dataOutputStream.writeInt(user.getPosts().size());// writing posts size
            user.getPosts().forEach(p -> {
                try {
                    dataOutputStream.writeLong(p == null ? (long)0 : p.getId());// writing post
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            dataOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User update(User user) {
        Long id = user.getId();
        List<User> lines = streamOfUsers().collect(Collectors.toList());
        clear();
        lines.forEach((u) -> {
            try {
                if (u.getId().equals(id)){
                    dataOutputStream.writeLong(user.getId());
                    dataOutputStream.writeUTF(user.getFirstName());
                    dataOutputStream.writeUTF(user.getLastName());
                    dataOutputStream.writeLong(user.getRegion() == null ? (long)0 : user.getRegion().getId());
                    dataOutputStream.writeUTF(user.getRole().toString());
                    dataOutputStream.writeInt(user.getPosts().size());
                    user.getPosts().stream().forEach(p -> {
                        try {
                            dataOutputStream.writeLong(p == null ? (long)0 : p.getId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                else {
                    dataOutputStream.writeLong(u.getId());
                    dataOutputStream.writeUTF(u.getFirstName());
                    dataOutputStream.writeUTF(u.getLastName());
                    dataOutputStream.writeLong(u.getRegion() == null ? (long)0 : u.getRegion().getId());
                    dataOutputStream.writeUTF(u.getRole().toString());
                    dataOutputStream.writeInt(u.getPosts().size());
                    u.getPosts().stream().forEach(p -> {
                        try {
                            dataOutputStream.writeLong(p == null ? (long)0 : p.getId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                dataOutputStream.flush();
            } catch (IOException e) {
                System.err.println("An error while updating");
            }
        });
        return user;
    }

    @Override
    public User getById(Long id) {
        return streamOfUsers()
                .filter(p -> p.getId().equals(id))
                .findAny().orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        List<User> lines = streamOfUsers().collect(Collectors.toList());

        clear();
        lines.forEach((u) -> {
            try {
                if (!id.equals(u.getId())) {
                    dataOutputStream.writeLong(u.getId());
                    dataOutputStream.writeUTF(u.getFirstName());
                    dataOutputStream.writeUTF(u.getLastName());
                    dataOutputStream.writeLong(u.getRegion() == null ? (long)0 : u.getRegion().getId());
                    dataOutputStream.writeUTF(u.getRole().toString());
                    dataOutputStream.writeInt(u.getPosts().size());
                    u.getPosts().stream().forEach(p -> {
                        try {
                            dataOutputStream.writeLong(p == null ? (long)0 : p.getId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    dataOutputStream.flush();
                }
            } catch (IOException e) {
                System.err.println("An error while deleting from users.txt");
            }
        });
    }

    @Override
    public List<User> getAll() {
        return streamOfUsers().collect(Collectors.toList());
    }

    private void clear() {
        try {
            new PrintWriter(file).close();
        } catch (IOException e) {
            System.err.println("Repository has not been cleared");
        }
    }

    private Stream<User> streamOfUsers(){
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            System.err.println("Can not find from users.txt");
        }
        List<User> listOfUsers = new ArrayList<>();
        while (true) {
            try {
                if (!(dis.available() > 0)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Long tempId = dis.readLong();//reading user ID
                String tempFirstName = dis.readUTF();// reading fn
                String tempLastName = dis.readUTF();// reading ln
                Long tempRegionId = dis.readLong();// reading region ID
                Role tempRole = Role.parseRole(dis.readUTF()); // reading role
                Region tempRegion = regionRepository.getById(tempRegionId);
                List<Post> tempPosts = new ArrayList<>();
                for(int i = dis.readInt(); i > 0; i--){// reading posts size
                    Long tempPostId = dis.readLong();//reading post id
                    if(!tempPostId.equals((long)0))
                        tempPosts.add(postRepository.getById(tempPostId));
                }
                listOfUsers.add(new User(tempId, tempFirstName, tempLastName, tempPosts, tempRegion, tempRole));
            } catch (IOException e) {
                System.out.println();
                e.printStackTrace(System.out);
                System.out.println();
            }
        }
        return listOfUsers.stream();

    }

    private Long generateId() {
        if(this.lastId == null){
            if (streamOfUsers().count() == 0) {
                lastId = (long) 0;
            } else {
                final Long[] lastId = {(long) -1};
                streamOfUsers().forEachOrdered((r -> lastId[0] = r.getId()));
                this.lastId = lastId[0];
            }
        }
        return ++lastId;
    }
}
