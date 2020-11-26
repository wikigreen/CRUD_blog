package com.vladimir.crudblog.repository;

import com.vladimir.crudblog.model.Post;
import com.vladimir.crudblog.model.Region;
import com.vladimir.crudblog.model.Role;
import com.vladimir.crudblog.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserRepository implements GenericRepository<User, Long>{
    private static UserRepository instance;
    private final File file;
    DataOutputStream dataOutputStream;
    private Long lastId;
    private PostRepository postRepository;
    private RegionRepository regionRepository;

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
            dataOutputStream.writeLong(user.getId());
            dataOutputStream.writeUTF(user.getFirstName());
            dataOutputStream.writeUTF(user.getLastName());
            dataOutputStream.writeLong(user.getRegion().getId());
            dataOutputStream.writeUTF(user.getRole().toString());
            dataOutputStream.writeInt(user.getPosts().size());
            user.getPosts().stream().forEach(p -> {
                try {
                    dataOutputStream.writeLong(p.getId());
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
                    dataOutputStream.writeLong(user.getRegion().getId());
                    dataOutputStream.writeUTF(user.getRole().toString());
                    dataOutputStream.writeInt(user.getPosts().size());
                    user.getPosts().stream().forEach(p -> {
                        try {
                            dataOutputStream.writeLong(p.getId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                else {
                    dataOutputStream.writeLong(u.getId());
                    dataOutputStream.writeUTF(u.getFirstName());
                    dataOutputStream.writeUTF(u.getLastName());
                    dataOutputStream.writeLong(u.getRegion().getId());
                    dataOutputStream.writeUTF(u.getRole().toString());
                    dataOutputStream.writeInt(u.getPosts().size());
                    u.getPosts().stream().forEach(p -> {
                        try {
                            dataOutputStream.writeLong(p.getId());
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
    public void deleteById(Long id) {
        List<User> lines = streamOfUsers().collect(Collectors.toList());

        clear();
        lines.forEach((u) -> {
            try {
                if (!id.equals(u.getId())) {
                    dataOutputStream.writeLong(u.getId());
                    dataOutputStream.writeUTF(u.getFirstName());
                    dataOutputStream.writeUTF(u.getLastName());
                    dataOutputStream.writeLong(u.getRegion().getId());
                    dataOutputStream.writeUTF(u.getRole().toString());
                    dataOutputStream.writeInt(u.getPosts().size());
                    u.getPosts().stream().forEach(p -> {
                        try {
                            dataOutputStream.writeLong(p.getId());
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

    public void deletePostById(Long id){
        List<User> lines = streamOfUsers().collect(Collectors.toList());
        Long tempId = lines.stream().flatMap(u -> u.getPosts().stream())
                .map(Post::getId)
                .filter(l -> l.equals(id))
                .findAny()
                .orElse((long)-1);
        if (tempId.equals(-1)){
            System.out.println("dont work");
            return;
        }
        System.out.println(tempId);
        clear();
        lines.forEach((u) -> {
            try {
                dataOutputStream.writeLong(u.getId());
                dataOutputStream.writeUTF(u.getFirstName());
                dataOutputStream.writeUTF(u.getLastName());
                dataOutputStream.writeLong(u.getRegion().getId());
                dataOutputStream.writeUTF(u.getRole().toString());
                dataOutputStream.writeInt(u.getPosts().size() - 1);
                u.getPosts().stream()
                        .forEach(p -> {
                    try {
                        if(!p.getId().equals(id))
                        dataOutputStream.writeLong(p.getId());
                        System.out.println(!p.equals(id) + " " + p.getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                dataOutputStream.flush();

            } catch (IOException e) {
                System.err.println("An error while deleting from users.txt");
            }
        });
    }

    @Override
    public List<User> getList() {
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
                Long tempId = dis.readLong();
                String tempFirstName = dis.readUTF();
                String tempLastName = dis.readUTF();
                Long tempRegionId = dis.readLong();
                Region tempRegion = regionRepository.getList().stream()
                        .filter(r -> r.getId().equals(tempRegionId))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("RegionRepository does not have region with id " + tempRegionId));
                Role tempRole = Role.parseRole(dis.readUTF());
                List<Long> tempPostIDs = new ArrayList<>();
                for(int i = dis.readInt(); i > 0; i--)
                    tempPostIDs.add(dis.readLong());
                List<Post> tempPosts = tempPostIDs.stream().map(l -> postRepository.getList().stream()
                .filter(p -> p.getId().equals(l))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("PostRepository does not have post with id " + l))).collect(Collectors.toList());
                listOfUsers.add(new User(tempId, tempFirstName, tempLastName, tempPosts, tempRegion, tempRole));
            } catch (IOException e) {
                System.out.println();
                e.printStackTrace(System.out);
                System.out.println();
            }
        }
        return listOfUsers.stream();

    }

    private Long generateId (){
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
