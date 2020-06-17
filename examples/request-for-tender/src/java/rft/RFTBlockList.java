// CArtAgO artifact code for project request-for-tender

package rft;

import jason.asSyntax.Atom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import cartago.Artifact;
import cartago.OPERATION;

public class RFTBlockList extends Artifact {


    static final String fileName = "rft-badlist.txt";
    static final String opId     = "bad_tender";


    BufferedWriter out;

    void init() {
        try {
            File f = new File(fileName);
            if (f.exists()) {
                BufferedReader in = new BufferedReader(new FileReader(fileName));
                String lin = in.readLine();
                while (lin != null) {
                    defineObsProperty(opId, new Atom(lin));
                    lin = in.readLine();
                }
                in.close();
            }
            out = new BufferedWriter(new FileWriter(fileName, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OPERATION void addBadTender(String ag) {
        defineObsProperty(opId, new Atom(ag));
        try {
            out.write(ag+"\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
