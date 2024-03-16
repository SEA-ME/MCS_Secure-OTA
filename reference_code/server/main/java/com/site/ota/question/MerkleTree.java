package com.site.ota.question;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MerkleTree { // 클래스 이름 변경
    private static final String HASH_ALGORITHM = "SHA-256";
    private MessageDigest digest;

    public MerkleTree() throws NoSuchAlgorithmException { // 생성자 이름 변경
        digest = MessageDigest.getInstance(HASH_ALGORITHM);
    }

    public byte[] computeHash(byte[] data) {
        return digest.digest(data);
    }

    public byte[] buildTree(byte[][] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        if (data.length == 1) {
            return computeHash(data[0]);
        }

        int mid = data.length / 2;
        byte[][] leftData = new byte[mid][];
        byte[][] rightData = new byte[data.length - mid][];

        System.arraycopy(data, 0, leftData, 0, mid);
        System.arraycopy(data, mid, rightData, 0, data.length - mid);

        byte[] leftHash = buildTree(leftData);
        byte[] rightHash = buildTree(rightData);

        return computeHash(concatenateHashes(leftHash, rightHash));
    }

    private byte[] concatenateHashes(byte[] hash1, byte[] hash2) {
        byte[] concatenated = new byte[hash1.length + hash2.length];
        System.arraycopy(hash1, 0, concatenated, 0, hash1.length);
        System.arraycopy(hash2, 0, concatenated, hash1.length, hash2.length);
        return concatenated;
    }
}
