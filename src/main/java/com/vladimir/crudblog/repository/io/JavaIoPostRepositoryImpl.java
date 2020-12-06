package com.vladimir.crudblog.repository.io;

import com.vladimir.crudblog.model.Post;
import com.vladimir.crudblog.repository.PostRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaIoPostRepositoryImpl implements PostRepository {
    private static JavaIoPostRepositoryImpl instance;
    private final String POST_FILE_PATH = "src//main//resources//files//posts.txt";
    private final File file;
    DataOutputStream dataOutputStream;
    private Long lastId;

    private JavaIoPostRepositoryImpl() throws IOException {
        file = new File(POST_FILE_PATH);
        dataOutputStream = new DataOutputStream(new FileOutputStream(file, true));
    }

    public static JavaIoPostRepositoryImpl getInstance() {
        if (instance == null) {
            try {
                instance = new JavaIoPostRepositoryImpl();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == null)
            post.setId(generateId());
        try {
            dataOutputStream.writeLong(post.getId());
            dataOutputStream.writeUTF(post.getContent());
            dataOutputStream.writeLong(post.getCreated().getTime());
            dataOutputStream.writeLong(post.getUndated().getTime());
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }


    @Override
    public Post update(Post post) {
        streamOfPosts()
                .filter(p -> p.getId().equals(post.getId()))
                .findAny().orElseThrow(() -> new IllegalArgumentException("There is no region with id " + post.getId()));

        Long id = post.getId();
        List<Post> lines = streamOfPosts().collect(Collectors.toList());
        clear();
        lines.forEach((p) -> {
                    try {
                        if (p.getId().equals(id)){
                            dataOutputStream.writeLong(post.getId());
                            dataOutputStream.writeUTF(post.getContent());
                            dataOutputStream.writeLong(p.getCreated().getTime());
                            dataOutputStream.writeLong(new Date().getTime());
                        }
                        else {
                            dataOutputStream.writeLong(p.getId());
                            dataOutputStream.writeUTF(p.getContent());
                            dataOutputStream.writeLong(p.getCreated().getTime());
                            dataOutputStream.writeLong(p.getUndated().getTime());
                        }
                        dataOutputStream.flush();
                    } catch (IOException e) {
                        System.err.println("An error while updating");
                    }
                });
        return post;
    }

    @Override
    public Post getById(Long id) {
        return streamOfPosts()
                .filter(p -> p.getId().equals(id))
                .findAny().orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        streamOfPosts()
                .filter(p -> p.getId().equals(id))
                .findAny().orElseThrow(() -> new IllegalArgumentException("There is no region with id " + id));
        List<Post> lines = streamOfPosts().collect(Collectors.toList());

        clear();
        lines.forEach((p) -> {
                    try {
                        if (!id.equals(p.getId())) {
                            dataOutputStream.writeLong(p.getId());
                            dataOutputStream.writeUTF(p.getContent());
                            dataOutputStream.writeLong(p.getCreated().getTime());
                            dataOutputStream.writeLong(p.getUndated().getTime());
                            dataOutputStream.flush();
                        }
                    } catch (IOException e) {
                        System.err.println("An error while deleting from posts.txt");
                    }
                });
    }

    @Override
    public List<Post> getAll() {
        return streamOfPosts().collect(Collectors.toList());
    }

    private void clear() {
        try {
            new PrintWriter(file).close();
        } catch (IOException e) {
            System.err.println("Repository has not been cleared");
        }
    }

    private Stream<Post> streamOfPosts(){
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            System.err.println("Can not find from posts.txt");
        }
        List<Post> listOfPosts = new ArrayList<>();
        while (true) {
            try {
                if (!((dis != null ? dis.available() : 0) > 0)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                listOfPosts.add(new Post(dis.readLong(), dis.readUTF(), new Date(dis.readLong()), new Date(dis.readLong())));
            } catch (IOException e) {
                System.err.println("Error while reading from posts.txt");
            }
        }
        return listOfPosts.stream();

    }

    private Long generateId() {
        if(this.lastId == null){
            lastId = streamOfPosts()
                    .map(Post::getId)
                    .max(Long::compareTo)
                    .orElse(0L);
        }
        return ++lastId;
    }
}
