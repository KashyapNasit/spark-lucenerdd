package com.erudika.lucene.store.s3;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try {
            Directory directory = new S3Directory("lucene-test", "lucene-kashyap");

            System.out.println(Arrays.toString(directory.listAll()));

            System.out.println(directory.fileLength("_b_Lucene84_0.doc"));

            System.out.println(directory.openChecksumInput("_b_Lucene84_0.doc", new IOContext()).getChecksum());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
