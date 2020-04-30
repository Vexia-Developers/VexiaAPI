package fr.vexia.proxy.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TabComplete {

    private String args;
    private List<String> list;

    public TabComplete(String args, List<String> list) {
        this.args = args;
        this.list = list;
    }

    public TabComplete(String args, Collection<String> list) {
        this.args = args;
        this.list = new ArrayList<>(list);
    }

    public List<String> getReponse(){
        List<String> list = new ArrayList<>();
        for (String value : this.list) {
            if(value.toLowerCase().startsWith(args.toLowerCase())){
                list.add(value);
            }
        }
        return list;
    }

}
