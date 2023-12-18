package com.example.securingweb.bean;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DocRegistry {
    public List<String> getDocuments() {
        Map<String, List<String>> documentList = queryDocuments();
        List<String> resultDocList = new ArrayList<>();

        // Getting alliance context
        List<String> userGroup = AllianceContext.getCurrentAlliance();


        if (userGroup != null && documentList != null)
        {
            System.out.println("Current Alliance ");
            userGroup.forEach(System.out::println);
            if(userGroup.contains("PERMIT_ALL"))
            {
                System.out.println("superadmin role has permit all ");
                resultDocList = documentList.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
            }
            else
            {
                for (String groupPath : userGroup) {

                    String group = groupPath.replace("/", "");

                    if (documentList.containsKey(group)) {
                        resultDocList.addAll(documentList.get(group));
                    } else {
                        System.out.println("No docs present for Alliance : " + group);
                    }
                }
            }
        } else if (documentList != null) {
            // Getting all Docs
            System.out.println("Current Alliance in NULL, user not added to any Group");
        }

        return resultDocList;
    }

    private Map<String, List<String>> queryDocuments() {
        Map<String, List<String>> documentList = new HashMap<>();
        documentList.put("American Funds", Arrays.asList("af-doc-1", "af-doc-2"));
        documentList.put("Vanguard", Arrays.asList("vg-doc-1", "vg-doc-2", "vg-doc-3"));

        return documentList;
    }
}
