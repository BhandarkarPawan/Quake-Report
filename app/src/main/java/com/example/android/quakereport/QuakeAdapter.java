package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Pawan on 13-04-2018.
 */

public class QuakeAdapter extends ArrayAdapter<earthquake> {


    public QuakeAdapter(@NonNull Context context,@NonNull ArrayList<earthquake> Quakes) {
        super(context, 0, Quakes);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentView = convertView;
        if(currentView==null)
        {
            currentView  = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);


        }

        final earthquake currentQuake = getItem(position);
        String mag = currentQuake.getmMagnitude();
        TextView M = (TextView) currentView.findViewById(R.id.MagnitudeTextView);
        M.setText(mag);

        GradientDrawable gradientDrawable = (GradientDrawable)M.getBackground();
        gradientDrawable.setColor(ContextCompat.getColor(getContext(),(getMagnitudeColor(Double.parseDouble(mag)))));

        TextView O = (TextView) currentView.findViewById(R.id.OffsetTextView) ;

        TextView L = (TextView) currentView.findViewById(R.id.LocationTextView);
        String location = currentQuake.getmLocation();
        if(location.contains(" of ")) {
            String[] locString = location.split(" of ");
            Log.v("OFFSET: ", locString[0]);
            Log.v("CENTER: ", locString[1]);
            O.setText(locString[0]+ " of " );
            L.setText(locString[1]);
        }
        else
            L.setText(location);




        long  time = currentQuake.getmTime();
        Date epochTime = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        String dateString =  sdf.format(epochTime);
        sdf = new SimpleDateFormat("h:mm aa");
        String timeString = sdf.format(epochTime);
        Log.v("DATE ", position+ ": "+ dateString);
        Log.v("TIME ", position+ ": "+ timeString);
        TextView D = (TextView)currentView.findViewById(R.id.DateTextView);
        D.setText(dateString);
        TextView T = (TextView) currentView.findViewById(R.id.TimeTextView);
        T.setText(timeString);

        currentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentQuake.getmUrl()));
                getContext().startActivity(intent);
            }
        });

        return currentView;


    }


    private int getMagnitudeColor(double mag){
        switch ((int)Math.floor(mag))
        {   case 0:
            case 1: return R.color.magnitude1;
            case 2: return R.color.magnitude2;
            case 3: return R.color.magnitude3;
            case 4: return R.color.magnitude4;
            case 5: return R.color.magnitude5;
            case 6: return R.color.magnitude6;
            case 7: return R.color.magnitude7;
            case 8: return R.color.magnitude8;
            case 9: return R.color.magnitude9;
            default: return R.color.magnitude10plus;


        }
    }
}
