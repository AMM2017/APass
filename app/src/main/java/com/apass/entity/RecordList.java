package com.apass.entity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Ivan on 12.11.2017.
 */

public class RecordList implements Serializable {
    private List<Record> list;

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

    public String[] getNames() {
        ArrayList<String> strings = new ArrayList<String>();
        for (Record r: list) {
            strings.add(r.getName());
        }
        return strings.toArray(new String[0]);
    }

    public void save(ContextWrapper context) {

        byte[] pass = "pass1111".getBytes();

        try {
            SecretKeySpec key = new SecretKeySpec(pass, "DES");
            Cipher ecipher = Cipher.getInstance("DES");

            ecipher.init(Cipher.ENCRYPT_MODE, key);




            SealedObject so = new SealedObject((Serializable)list, ecipher);


            File file = new File(context.getFilesDir(), "temp.out");
            file.createNewFile();
            //File file = new File();
            FileOutputStream outputStream = context.openFileOutput("temp.out", Context.MODE_PRIVATE);
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

    public void load(ContextWrapper context) {

        byte[] pass = "pass1111".getBytes();

        try {
            SecretKeySpec key = new SecretKeySpec(pass, "DES");



            Cipher dcipher = Cipher.getInstance("DES");
            dcipher.init(Cipher.DECRYPT_MODE, key);
            //SealedObject so = new SealedObject(this, cipher);



            FileInputStream fis = context.openFileInput("temp.out");
            ObjectInputStream ois = new ObjectInputStream(fis);
            SealedObject so = (SealedObject) ois.readObject();

            ois.close();
            list = (LinkedList<Record>)so.getObject(dcipher);


        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }  catch (BadPaddingException e) {
            e.printStackTrace();
        }


    }
}
