package ru.geekbrains.android.network.picasso;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ImageWeather {
   private static List<String> urlList = new ArrayList();
    public void getFon(ImageView imageView){
        Picasso.get()
                .load(getRandomUrl())
                .fit()      // уменьшает размер картинки перед размещением
                .centerCrop()
                .into(imageView);
    }

    private String getRandomUrl() {


        urlList.add("https://images.unsplash.com/photo-1469829638725-69bf13ad6801?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60");
        urlList.add("https://images.unsplash.com/photo-1501588647130-2e99f4585623?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80");
        urlList.add("https://images.unsplash.com/photo-1445285303476-2f3b16d67b7a?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=750&q=80");
        urlList.add("https://images.unsplash.com/photo-1437650128481-845e6e35bd75?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=750&q=80");
        urlList.add("https://images.unsplash.com/photo-1527505866929-e8b0baa6b680?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=415&q=80");
        urlList.add("https://images.unsplash.com/photo-1600175731142-d260bb77a3ea?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=334&q=80");
        urlList.add("https://images.unsplash.com/photo-1537805315776-d2ad319bedb0?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=750&q=80");
        urlList.add("https://images.unsplash.com/photo-1538330447121-4c41b85862f5?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=334&q=80");
        urlList.add("https://images.unsplash.com/photo-1467094568967-95f87ee9c873?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=750&q=80");
        urlList.add("https://images.unsplash.com/photo-1464013778555-8e723c2f01f8?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=750&q=80");
        urlList.add("https://images.unsplash.com/photo-1458571037713-913d8b481dc6?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=755&q=80");
        urlList.add("https://images.unsplash.com/photo-1458898257815-0ec6bfaa0ade?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=750&q=80");

        return urlList.get( new Random().nextInt(urlList.size()));
    }
}
