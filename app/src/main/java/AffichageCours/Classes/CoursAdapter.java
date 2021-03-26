package AffichageCours.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appmobilev2.R;

import java.util.ArrayList;
import java.util.List;

public class CoursAdapter extends ArrayAdapter<Cours> {
    private Context mContext;
    private List<Cours> moviesList = new ArrayList<>();

    public CoursAdapter(@NonNull Context context, @NonNull List<Cours> objects) {
        super(context, 0, objects);
        mContext = context;
        moviesList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);

        Cours courss = moviesList.get(position);
        TextView nomCours = (TextView) listItem.findViewById(R.id.textViewCours);
        nomCours.setText(courss.getNomCours());
        TextView debut = (TextView) listItem.findViewById(R.id.textViewDebutHeure);
        debut.setText(courss.getDebutCours());
        TextView fin = (TextView) listItem.findViewById(R.id.textViewFinHeure);
        fin.setText(courss.getFinCours());
        return listItem;
    }

}
