package com.androiddvptteam.helpme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

public class MessageFragment extends Fragment
{
    private static View view;
    public List<Message> message = new LinkedList<>();
    private MessageAdapter adapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.message_fragment, container, false);
        final MyApplication myApplication = (MyApplication) getActivity().getApplication();
        message = myApplication.getMessage();
        adapter = new MessageAdapter(getContext(), R.layout.message_item, message, getActivity());
        ListView listView = (ListView) view.findViewById(R.id.message_listView);
        listView.setAdapter(adapter);
        return view;
    }
}
