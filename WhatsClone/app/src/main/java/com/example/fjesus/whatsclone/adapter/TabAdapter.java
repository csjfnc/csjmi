package com.example.fjesus.whatsclone.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.fjesus.whatsclone.fragments.ContatosFragment;
import com.example.fjesus.whatsclone.fragments.ConversasFragment;

/**
 * Created by fjesus on 18/04/2017.
 */

public class TabAdapter extends FragmentStatePagerAdapter {


    String[] abas = {"CONVERSAS", "CONTATOS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        switch (position){
            case 0:{
                fragment = new ConversasFragment();
                break;
            }
            case 1:{
                fragment = new ContatosFragment() ;
                break;
            }
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return abas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return abas[position];
    }
}
