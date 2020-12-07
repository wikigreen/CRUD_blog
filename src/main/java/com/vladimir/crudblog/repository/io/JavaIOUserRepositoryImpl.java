package com.vladimir.crudblog.repository.io;

import com.vladimir.crudblog.model.Post;
import com.vladimir.crudblog.model.Region;
import com.vladimir.crudblog.model.Role;
import com.vladimir.crudblog.model.User;
import com.vladimir.crudblog.repository.GenericRepository;
import com.vladimir.crudblog.repository.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaIOUserRepositoryImpl implements UserRepository {
    private static JavaIOUserRepositoryImpl instance;
    private final String USER_FILE_PATH = "src//main//resources//files//users.txt";
    private final File file;
    DataOutputStream dataOutputStream;
    private Long lastId;
    private final JavaIoPostRepositoryImpl javaIoPostRepositoryImpl;
    private final JavaIORegionRepositoryImpl javaIORegionRepositoryImpl;

    private JavaIOUserRepositoryImpl() throws IOException {
        file = new File(USER_FILE_PATH);
        dataOutputStream = new DataOutputStream(new FileOutputStream(file, true));
        javaIoPostRepositoryImpl = JavaIoPostRepositoryImpl.getInstance();
        javaIORegionRepositoryImpl = JavaIORegionRepositoryImpl.getInstance();
    }

    public static JavaIOUserRepositoryImpl getInstance() {
        if (instance == null) {
            try {
                instance = new JavaIOUserRepositoryImpl();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private void writeData(User user) throws IOException {
        dataOutputStream.writeLong(user.getId());
        dataOutputStream.writeUTF(user.getFirstName());
        dataOutputStream.writeUTF(user.getLastName());
        dataOutputStream.writeLong(user.getRegion() == null ? 0L : user.getRegion().getId());
        dataOutputStream.writeUTF(user.getRole().toString());
        dataOutputStream.writeInt(user.getPosts().size());
    }

    @Override
    public User save(User user) {
        if (user.getId() == null)
            user.setId(generateId());
        try {
            writeData(user);
            user.getPosts().forEach(p -> {
                try {
                    dataOutputStream.writeLong(p == null ? 0L : p.getId());
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
                    writeData(user);
                    user.getPosts().forEach(p -> {
                        try {
                            dataOutputStream.writeLong(p == null ? 0L : p.getId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                else {
                    writeData(u);
                    u.getPosts().forEach(p -> {
                        try {
                            dataOutputStream.writeLong(p == null ? 0L : p.getId());
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
                    writeData(u);
                    u.getPosts().forEach(p -> {
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
                Long tempId = dis.readLong();
                String tempFirstName = dis.readUTF();
                String tempLastName = dis.readUTF();
                Long tempRegionId = dis.readLong();
                Role tempRole = Role.parseRole(dis.readUTF());
                Region tempRegion = javaIORegionRepositoryImpl.getById(tempRegionId);
                List<Post> tempPosts = new ArrayList<>();
                for(int i = dis.readInt(); i > 0; i--){
                    Long tempPostId = dis.readLong();
                    if(!tempPostId.equals((long)0))
                        tempPosts.add(javaIoPostRepositoryImpl.getById(tempPostId));
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
            lastId = streamOfUsers()
                    .map(User::getId)
                    .max(Long::compareTo)
                    .orElse(0L);
        }
        return ++lastId;
    }
}
