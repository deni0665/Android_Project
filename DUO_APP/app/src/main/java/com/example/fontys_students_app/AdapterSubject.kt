package com.example.fontys_students_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterSubject(subjectList:ArrayList<Subject>) : RecyclerView.Adapter<AdapterSubject.ExampleViewHolder>() {

    private var list: ArrayList<Subject>? = subjectList;

    private var mListener: OnItemClickListener?=null;

    interface OnItemClickListener {
        fun onItemClick(position:Int)
    }
    public fun setOnItemClickListener(listener: OnItemClickListener)
    {
        mListener=listener;
    }

    public class ExampleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var mImageView = itemView.findViewById<ImageView>(R.id.imageViewSubject)
        var mName = itemView.findViewById<TextView>(R.id.TextView1);
        var explain = itemView.findViewById<TextView>(R.id.TextView2);

        constructor(itemView: View,ClickListener: OnItemClickListener) : this(itemView) {
            itemView.setOnClickListener(object : View.OnClickListener {
                @Override
                override fun onClick(v: View) {
                    if (ClickListener != null) {
                        var position: Int = adapterPosition;
                        if (position != RecyclerView.NO_POSITION) {
                            ClickListener.onItemClick(position);
                        }
                    }
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        var v:View= LayoutInflater.from(parent.context).inflate(R.layout.example_subject,parent,false)
        var ehv:ExampleViewHolder = ExampleViewHolder(v,mListener!!)
        return ehv;
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        var currentSubject : Subject = list!!.get(position)

        holder.mImageView.setImageResource(currentSubject.getPicture());
        holder.mName.setText((currentSubject.getName()));
        holder.explain.setText(currentSubject.getExplanation());
    }
    public fun filterList(newlist:ArrayList<Subject>)
    {
        list =newlist;
        notifyDataSetChanged();
    }
}