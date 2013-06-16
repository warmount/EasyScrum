package org.sfsteam.easyscrum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.sfsteam.easyscrum.data.DeckDT;
import org.sfsteam.easyscrum.view.FlyOutContainer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by warmount on 12.06.13.
 */
public class SampleActivity extends Activity {
    private static final Object SUSPEND_FILE = "easyScrum.lst";
    FlyOutContainer root;
    ListView scrumList;
    MySimpleAdapter arrayAdapter;
    List<DeckDT> listStr;
    GridView gvMain;
    TextView hello;
    DeckDT deckInGrid;

    private boolean isEdit;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private boolean isNewOpen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.root = (FlyOutContainer) this.getLayoutInflater().inflate(R.layout.activity_sample, null);

        this.setContentView(root);

        scrumList = (ListView) findViewById(R.id.menu_listview);

        final File cache_dir = this.getCacheDir();
        final File suspend_f = new File(cache_dir.getAbsoluteFile() + File.separator + SUSPEND_FILE);
        listStr = (List<DeckDT>) loadSerializedList(suspend_f);
        if (listStr == null){
            listStr = new ArrayList<DeckDT>();
            listStr.add(new DeckDT("preplan","1,2,4,8,16"));
            listStr.add(new DeckDT("plan","1,2,3,5,8,13"));
        }
        arrayAdapter = new MySimpleAdapter(this, R.layout.list_layout,listStr);
        scrumList.setAdapter(arrayAdapter);

        final GestureDetector gestureDetector = new GestureDetector(this, new MyGestureListener());
        gvMain = (GridView) findViewById(R.id.gridView);
        gvMain.setOnTouchListener( new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        hello = (TextView) findViewById(R.id.hello_text);
        hello.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        ImageButton addButton = (ImageButton) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMenuClose(view);
                EditText deckName = (EditText) findViewById(R.id.deckName);
                EditText deckString = (EditText) findViewById(R.id.deckString);
                deckString.setText("");
                deckName.setText("");
                toggleNewOpen(view);
                isEdit=false;
            }
        });

    }

    public void toggleMenuClose(View v){
        isNewOpen = false;
        this.root.toggleMenuClose();
    }

    public void toggleNewOpen(View v){
        toggleMenuClose(v);
        isNewOpen = true;
        this.root.toggleNewOpen();
    }

    public void startCardActivity(View v, String value){
        Intent intent = new Intent(SampleActivity.this, CardActivity.class);
        Bundle b = new Bundle();
        b.putString("card", value); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
    }


    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (!isNewOpen){
                    isNewOpen=false;
                    root.toggleMenuClose();
                    return true;
                    }
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if (!isNewOpen){
                    arrayAdapter.notifyDataSetChanged();
                    root.toggleMenuOpen();

                    return true;
                    } else {
                        isNewOpen=false;
                        root.toggleMenuClose();
                        return true;
                    }
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

    }

    private class MySimpleAdapter extends ArrayAdapter<DeckDT>{
        List<DeckDT> decks;
        int editPosition;


        public MySimpleAdapter(Context context, int textViewResourceId, List<DeckDT> objects) {
            super(context, textViewResourceId, objects);
            this.decks =objects;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = null;
            LayoutInflater inflater = getLayoutInflater();

            row = inflater.inflate(R.layout.list_layout, parent, false);

            // inflate other items here :
            ImageButton deleteButton = (ImageButton) row.findViewById(R.id.del_button);
            deleteButton.setTag(position);

            deleteButton.setOnClickListener(
                    new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (deckInGrid !=null && deckInGrid.equals(decks.get(position))){
                                gvMain.setVisibility(View.GONE);
                                deckInGrid=null;
                                hello.setVisibility(View.VISIBLE);
                            }
                            arrayAdapter.remove(decks.get(position));
                        }
                    }
            );

            final TextView name = (TextView) row.findViewById(R.id.deck_list_name);
            name.setText(decks.get(position).getName());

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setCardsToGrid(decks.get(position));
                }
            });

            final EditText deckName = (EditText) findViewById(R.id.deckName);
            final EditText deckString = (EditText) findViewById(R.id.deckString);

            ImageButton editButton = (ImageButton) row.findViewById(R.id.edt_button);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    deckName.setText(decks.get(position).getName());
                    deckString.setText(decks.get(position).getDeckString());
                    toggleNewOpen(view);
                    isEdit=true;
                    editPosition = position;
                }
            });

            ImageButton okButton = (ImageButton) findViewById(R.id.ok_button);

            okButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    DeckDT dt = new DeckDT(deckName.getText().toString(),deckString.getText().toString());
                    if (isEdit){
                        List<DeckDT> listWO = new ArrayList<DeckDT>();
                        listWO.addAll(listStr);
                        listWO.remove(editPosition);
                        if (!isDeckUnique(dt, listWO)){
                            Toast.makeText(getApplicationContext(),R.string.have_deck,2).show();
                        } else {
                            decks.get(editPosition).setName(dt.getName());
                            decks.get(editPosition).setDeckString(dt.getDeckString());
                            setCardsToGrid(dt);
                            InputMethodManager imm = (InputMethodManager)getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(deckName.getWindowToken(), 0);
                            imm.hideSoftInputFromWindow(deckString.getWindowToken(), 0);
                        }
                    } else {
                        if (!isDeckUnique(dt, listStr)){
                            Toast.makeText(getApplicationContext(),R.string.have_deck,2).show();
                        } else {
                            arrayAdapter.add(dt);
                            setCardsToGrid(dt);
                            InputMethodManager imm = (InputMethodManager)getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(deckName.getWindowToken(), 0);
                            imm.hideSoftInputFromWindow(deckString.getWindowToken(), 0);
                        }
                    }
                }
            });

            return row;
        }



        private boolean isDeckUnique(DeckDT dt, List<DeckDT> listStr) {
            for (DeckDT dt2:listStr){
                if (dt2.getDeckString().equals(dt.getDeckString()) || dt2.getName().equals(dt.getName())){
                    return false;
                }
            }
            return true;
        }
    }

    private void setCardsToGrid(DeckDT dt) {
        deckInGrid = dt;
        ArrayAdapter<String> adapter = new MyArrayAdapter(getApplicationContext(), R.layout.grid_item, R.id.tvText, dt.getDeckAsArray());
        gvMain.setAdapter(adapter);
        gvMain.setVisibility(View.VISIBLE);
        hello.setVisibility(View.GONE);
        root.toggleMenuClose();
    }

    private class MyArrayAdapter extends ArrayAdapter<String> {
        String[] cards;
        public MyArrayAdapter(Context applicationContext, int grid_item, int tvText, String[] data) {
            super(applicationContext, grid_item,tvText,data);
            this.cards = data;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            View grid = inflater.inflate(R.layout.grid_item, parent, false);
            final TextView cardValue = (TextView) grid.findViewById(R.id.tvText);
            cardValue.setText(cards[position]);
            cardValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startCardActivity(view,cardValue.getText().toString());
                }
            });
            return grid;
        }
    }

    private void saveList() {
        final File cache_dir = this.getCacheDir();
        final File suspend_f = new File(cache_dir.getAbsoluteFile() + File.separator + SUSPEND_FILE);

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        boolean keep = true;

        try {
            fos = new FileOutputStream(suspend_f);
            oos = new ObjectOutputStream(fos);

            oos.writeObject(this.listStr);
        }
        catch (Exception e) {
            keep = false;
            Log.e("EasyScrum", "failed to suspend", e);
        }
        finally {
            try {
                if (oos != null)   oos.close();
                if (fos != null)   fos.close();
                if (keep == false) suspend_f.delete();
            }
            catch (Exception e) { /* do nothing */ }
        }
    }

    public Object loadSerializedList(File f)
    {
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Object o = ois.readObject();
            return o;
        }
        catch(Exception ex)
        {
            Log.e("EasyScrum","Serialization Read Error",ex);
        }
        return null;
    }

    @Override
    public void onPause(){
        super.onPause();
        saveList();
    }
}