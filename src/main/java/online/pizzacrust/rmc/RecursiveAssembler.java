package online.pizzacrust.rmc;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import online.pizzacrust.rmc.classes.ItemBaseClass;

public class RecursiveAssembler {

    private final File directory;
    private final File outputXmlFile;

    public RecursiveAssembler(File directory, File outputXmlFile) {
        this.directory = directory;
        this.outputXmlFile = outputXmlFile;
    }

    private String readFile(File file) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        bufferedReader.lines().forEach((l) -> {
            stringBuilder.append(l).append("\n");
        });
        return stringBuilder.toString();
    }

    private ItemBaseClass handle(File directory)  throws Exception{
        ItemBaseClass dir = new ItemBaseClass(new ArrayList<>(), "Folder", directory.getName(),
                new ArrayList<>(), null);
        File[] children = directory.listFiles();
        for (File child : children) {
            if (!child.isDirectory()) {
                if (child.getName().endsWith(".localscript.lua")) {
                    dir.getChildrenClasses().add(new ItemBaseClass(new ArrayList<>(),
                            "LocalScript", child.getName().replace(".localscript.lua", ""), new
                            ArrayList<>(), readFile
                            (child)));
                    continue;
                }
                if (child.getName().endsWith(".value.json")) {
                    ValuePojo pojo = new Gson().fromJson(readFile(child), ValuePojo.class);
                    ItemBaseClass itemBaseClass = new ItemBaseClass(new ArrayList<>(), pojo.type
                            .getClassName(), pojo.name, new ArrayList<>(), null);
                    itemBaseClass.getValues().add(new ItemBaseClass.PropertyValue(pojo.type
                            .getPrimitiveType(), "Value", pojo.value));
                    dir.getChildrenClasses().add(itemBaseClass);
                    continue;
                }
                dir.getChildrenClasses().add(new ItemBaseClass(new ArrayList<>(),
                        "Script", child.getName().replace(".lua", ""), new ArrayList<>(), readFile
                        (child)));
                continue;

            }

            ItemBaseClass itemBaseClass = handle(child);
            dir.getChildrenClasses().add(itemBaseClass);
        }
        return dir;
    }

    public void compile() throws Exception {
        File[] rootFiles = directory.listFiles();
        ItemBaseClass model = new ItemBaseClass(new ArrayList<>(), "Model", "RA-RMC-Artifact",
                new ArrayList<>(), null);
        for (File rootFile : rootFiles) {
            if (!rootFile.isDirectory()) {
                if (rootFile.getName().endsWith(".localscript.lua")) {
                    model.getChildrenClasses().add(new ItemBaseClass(new ArrayList<>(),
                            "LocalScript", rootFile.getName().replace(".localscript.lua", ""), new ArrayList<>(), readFile(rootFile)));
                    continue;
                }
                if (rootFile.getName().endsWith(".value.json")) {
                    ValuePojo pojo = new Gson().fromJson(readFile(rootFile), ValuePojo.class);
                    ItemBaseClass itemBaseClass = new ItemBaseClass(new ArrayList<>(), pojo.type
                            .getClassName(), pojo.name, new ArrayList<>(), null);
                    itemBaseClass.getValues().add(new ItemBaseClass.PropertyValue(pojo.type
                            .getPrimitiveType(), "Value", pojo.value));
                    model.getChildrenClasses().add(itemBaseClass);
                    continue;
                }
                model.getChildrenClasses().add(new ItemBaseClass(new ArrayList<>(),
                        "Script", rootFile.getName().replace(".lua", ""), new ArrayList<>(), readFile(rootFile)));
                continue;
            }
            ItemBaseClass itemBaseClass = handle(rootFile);
            model.getChildrenClasses().add(itemBaseClass);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<roblox xmlns:xmime=\"http://www.w3.org/2005/05/xmlmime\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xsi:noNamespaceSchemaLocation=\"http://www.roblox.com/roblox.xsd\" version=\"4\">\n");
        stringBuilder.append(model.getXML());
        stringBuilder.append("</roblox>");
        Files.write(outputXmlFile.toPath(), stringBuilder.toString().getBytes(),
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
    }

    public static void main(String... args) throws Exception {
        RecursiveAssembler recursiveAssembler = new RecursiveAssembler(new File("luatest"), new
                File
                ("output.xml"));
        recursiveAssembler.compile();
    }
}
