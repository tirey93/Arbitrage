package com.example.damianpytkowski.arbitrage_android;

import java.util.Random;

class Article {
    private String mTitle;
    private String mContent;

    // statyczne tablice, na podstawie których zostaną uzupełnione obiekty artykułów
    private static String[] sTitles = {"Lorem ipsum dolor sit amet",
            "Etiam sit", "Cras vel lorem",
            "Cras suscipit, urna at aliquam rhoncus",
            "Phasellus congue lacus eget neque",
            "Phasellus pharetra nulla ac diam"};

    private static String[] sContents = {"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin nibh augue, suscipit a, scelerisque sed, lacinia in, mi. Cras vel lorem. Etiam pellentesque aliquet tellus. Phasellus pharetra nulla ac diam.",
            "Quisque semper justo at risus. Donec venenatis, turpis vel hendrerit interdum, dui ligula ultricies purus, sed posuere libero dui id orci",
            "Nam congue, pede vitae dapibus aliquet, elit magna vulputate arcu, vel tempus metus leo non est. Etiam sit amet lectus quis est congue mollis.",
            "Phasellus congue lacus eget neque. Phasellus ornare, ante vitae consectetuer consequat, purus sapien ultricies dolor, et mollis pede metus eget nisi.",
            "Praesent sodales velit quis augue. Cras suscipit, urna at aliquam rhoncus, urna quam viverra nisi, in interdum massa nibh nec erat."};

    public Article() {
        Random random = new Random();

        // ustawiamy losowy tytuł i treść artykułu
        mTitle = sTitles[random.nextInt(sTitles.length)];
        mContent = sContents[random.nextInt(sContents.length)];
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }
}
