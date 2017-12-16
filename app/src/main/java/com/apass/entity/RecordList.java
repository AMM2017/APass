package com.apass.entity;

import android.content.Context;
import android.content.ContextWrapper;

import com.apass.tools.RecordComparator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private List<Record> list;
    //TEST
    //private  String  password = "1234567812345678";
    private byte[] passsha256;

    private static final String chipher = "AES";

    public static final String fileName = "datacpt";

    //private ContextWrapper contextWrapper;

    public RecordList() {
        list = new LinkedList<Record>();
    }

    public void add(Record r) {
        list.add(r);
    }

    public void remove(Record r) {
        list.remove(r);
    }

    public void  clear() {
        list.clear();
    }

    public Record get(int i) {
        return list.get(i);
    }

    public Record get(String name) {
        for (Record r: list) {
            if (r.getName().equals(name))
                return r;
        }
        return  null;
    }

    public byte[] getSha() { return passsha256; }

    public String[] getNames() {
        ArrayList<String> strings = new ArrayList<String>();
        for (Record r: list) {
            strings.add(r.getName());
        }
        return strings.toArray(new String[0]);
    }

    public void save(ContextWrapper context) {
        //сортировка перед сохранением
        Collections.sort(list, new RecordComparator());
        try {
            SecretKeySpec key = new SecretKeySpec(passsha256, chipher);
            Cipher ecipher = Cipher.getInstance(chipher);

            ecipher.init(Cipher.ENCRYPT_MODE, key);


            SealedObject so = new SealedObject((Serializable)list, ecipher);


            File file = new File(context.getFilesDir(), fileName + "temp");
            file.createNewFile();
            //File file = new File();
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(so);
            oos.flush();
            oos.close();

        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }


    }

//    public void load(ContextWrapper context, String pass) {
//        //contextWrapper = context;
//        try {
//            SecretKeySpec key = new SecretKeySpec(passsha256, chipher);
//
//            Cipher dcipher = Cipher.getInstance(chipher);
//            dcipher.init(Cipher.DECRYPT_MODE, key);
//            //SealedObject so = new SealedObject(this, cipher);
//
//            FileInputStream fis = context.openFileInput(fileName);
//            ObjectInputStream ois = new ObjectInputStream(fis);
//            SealedObject so = (SealedObject) ois.readObject();
//
//            ois.close();
//            list = (LinkedList<Record>)so.getObject(dcipher);
//
//
//        }
//        catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        }  catch (BadPaddingException e) {
//            e.printStackTrace();
//        }
//
//
//    }

    public void open(ContextWrapper context, String pass) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, ClassNotFoundException, BadPaddingException, IllegalBlockSizeException {

        passsha256 = getHashFromString(pass);

        SecretKeySpec key = new SecretKeySpec(passsha256, chipher);

        Cipher dcipher = Cipher.getInstance(chipher);
        dcipher.init(Cipher.DECRYPT_MODE, key);
        //SealedObject so = new SealedObject(this, cipher);

        FileInputStream fis = context.openFileInput(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        SealedObject so = (SealedObject) ois.readObject();

        ois.close();
        list = (LinkedList<Record>)so.getObject(dcipher);
    }

    public void create(ContextWrapper context, String pass) {
        passsha256 = getHashFromString(pass);
        save(context);
    }

    private byte[] getHashFromString(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(s.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public RecordList GetFilteredList(String mask) {

        String pattern = ".*" + mask + ".*";

        RecordList result = new RecordList();
        for (Record r: list) {
            if (Pattern.matches(pattern, r.getName()))
                result.add(r);
        }
        return result;
    }
}
