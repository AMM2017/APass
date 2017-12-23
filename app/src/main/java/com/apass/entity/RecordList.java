package com.apass.entity;

import android.content.Context;
import android.content.ContextWrapper;

import com.apass.tools.RecordComparator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Ivan on 12.11.2017.
 */

public class RecordList implements Serializable {
    public static final String fileName = "datacpt";
    private static final String chipher = "AES";
    private List<Record> list;
    private byte[] passsha256;

    public RecordList() {
        list = new LinkedList<>();
    }

    public void add(Record r) {
        list.add(r);
    }

    public void remove(Record r) {
        list.remove(r);
    }

    public void clear() {
        list.clear();
    }

    public Record get(int i) {
        return list.get(i);
    }

    public Record get(String name) {
        for (Record r : list) {
            if (r.getName().equals(name))
                return r;
        }
        return null;
    }

    public byte[] getSha() {
        return passsha256;
    }

    public String[] getNames() {
        ArrayList<String> strings = new ArrayList<>();
        for (Record r : list) {
            strings.add(r.getName());
        }
        return strings.toArray(new String[0]);
    }

    public void save(ContextWrapper context) {
        //сортировка перед сохранением
        Collections.sort(list, new RecordComparator());
        try {
            //создается секретный ключ из пароля с использованием определнного метода шифрования
            SecretKeySpec key = new SecretKeySpec(passsha256, chipher);
            //создание объекта, обеспечивающего шифрование определенным методом
            Cipher ecipher = Cipher.getInstance(chipher);
            //шифратор настраивается на шифрование данным ключом
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            //создается шифрованный объект-оболочка списка
            SealedObject so = new SealedObject((Serializable) list, ecipher);

            File file = new File(context.getFilesDir(), fileName);
            file.createNewFile();
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(so);
            oos.flush();
            oos.close();

        } catch (NoSuchAlgorithmException | IOException | NoSuchPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }


    }

    public void open(ContextWrapper context, String pass) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, ClassNotFoundException, BadPaddingException, IllegalBlockSizeException {

        passsha256 = getHashFromString(pass);
        //создается секретный ключ из пароля с использованием определнного метода шифрования
        SecretKeySpec key = new SecretKeySpec(passsha256, chipher);
        //создание объекта, обеспечивающего шифрование определенным методом
        Cipher dcipher = Cipher.getInstance(chipher);
        //шифратор настраивается на дешифрование данным ключом
        dcipher.init(Cipher.DECRYPT_MODE, key);

        FileInputStream fis = context.openFileInput(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        SealedObject so = (SealedObject) ois.readObject();

        ois.close();
        list = (LinkedList<Record>) so.getObject(dcipher);
    }

    public void create(ContextWrapper context, String pass) {
        passsha256 = getHashFromString(pass);
        save(context);
    }

    public boolean delete(Context context) {
        File file = new File(context.getFilesDir(), fileName);
        if (file.delete()) {
            clear();
            return true;
        }
        return false;

    }

    private byte[] getHashFromString(String s) {
        try {
            // класс MessageDigest обеспечивает приложения функциональность алгоритма обзора сообщения
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(s.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RecordList GetFilteredList(String mask) {

        String pattern = ".*" + mask + ".*";

        RecordList result = new RecordList();
        for (Record r : list) {
            if (Pattern.matches(pattern, r.getName()))
                result.add(r);
        }
        return result;
    }
}
