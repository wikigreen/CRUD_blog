package com.vladimir.crudblog.repository;

import com.vladimir.crudblog.model.Post;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PostRepository implements GenericRepository <Post, Long>{
    private static PostRepository instance;
    private final File file;
    DataOutputStream dataOutputStream;
    private Long lastId;

    private PostRepository() throws IOException {
        file = new File("src//main//resources//files//posts.txt");
        dataOutputStream = new DataOutputStream(new FileOutputStream(file, true));
    }

    public static PostRepository getInstance() {
        if (instance == null) {
            try {
                instance = new PostRepository();
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
        Long id = post.getId();
        List<Post> lines = streamOfPosts().collect(Collectors.toList());
        clear();
        lines.forEach((p) -> {
                    try {
                        if (p.getId().equals(id)){
                            dataOutputStream.writeLong(post.getId());
                            dataOutputStream.writeUTF(post.getContent());
                            dataOutputStream.writeLong(post.getCreated().getTime());
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
    public void deleteById(Long id) {
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
    public List<Post> getList() {
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
                if (!(dis.available() > 0)) break;
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

    private Long generateId (){
        if(this.lastId == null){
            if (streamOfPosts().count() == 0) {
                lastId = (long) 0;
            } else {
                final Long[] lastId = {(long) -1};
                streamOfPosts().forEachOrdered((r -> lastId[0] = r.getId()));
                this.lastId = lastId[0];
            }
        }
        return ++lastId;
    }
}
