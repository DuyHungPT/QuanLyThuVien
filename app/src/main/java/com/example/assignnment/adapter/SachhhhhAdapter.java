package com.example.assignnment.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignnment.R;
import com.example.assignnment.dao.SachhDDAO;
import com.example.assignnment.model.Sach;

import java.util.ArrayList;
import java.util.HashMap;

public class SachhhhhAdapter extends RecyclerView.Adapter<SachhhhhAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Sach> list;
    private ArrayList<HashMap<String,Object>> listHM;
    private SachhDDAO sachhDDAO;



    public SachhhhhAdapter(Context context, ArrayList<Sach> list,ArrayList<HashMap<String,Object>> listHM,SachhDDAO sachhDDAO) {
        this.context = context;
        this.list = list;
        this.listHM = listHM;
        this.sachhDDAO = sachhDDAO;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_recycle_sach,parent,false);





        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtMaSach.setText("Ma Sach"+ list.get(position).getMaSach());
        holder.txtTenSach.setText("Ten Sach"+ list.get(position).getTenSach());
        holder.txtGiaThue.setText("Gia Thue Sach"+ list.get(position).getGiaThue());
        holder.txtMaLoai.setText("Ma Loai"+ list.get(position).getMaLoai());
        holder.txtTenLoai.setText("Ten Loai"+ list.get(position).getTenloai());

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(list.get(holder.getAdapterPosition()));
            }
        });
        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check = sachhDDAO.xoaSach(list.get(holder.getAdapterPosition()).getMaSach());
           switch (check){
               case 1:
                   Toast.makeText(context, "xoa thanh cong", Toast.LENGTH_SHORT).show();
                   loadData();
                   break;
               case 0:
                   Toast.makeText(context, "xoa that bai", Toast.LENGTH_SHORT).show();
                  break;
               case -1:
                   Toast.makeText(context, "Sach Nay Khong xoa Duoc", Toast.LENGTH_SHORT).show();
                   break;
               default:
                   break;
           }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaSach,txtTenSach, txtGiaThue,txtMaLoai,txtTenLoai;
        ImageView ivEdit,ivDel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            txtMaSach = itemView.findViewById(R.id.txtMaSach);
            txtTenSach = itemView.findViewById(R.id.txtTenSach);
            txtGiaThue = itemView.findViewById(R.id.txtGiaThueSach);
            txtMaLoai = itemView.findViewById(R.id.txtMaLoai);
            txtTenLoai = itemView.findViewById(R.id.txtTenLoai);

            ivDel =itemView.findViewById(R.id.ivDel);
            ivEdit = itemView.findViewById(R.id.ivEdit);
        }
    }
   public void showDialog(Sach sach){
       AlertDialog.Builder builder = new AlertDialog.Builder(context);
       LayoutInflater inflater = ((Activity)context).getLayoutInflater();
       View view = inflater.inflate(R.layout.dialog_suasach,null);
       builder.setView(view);

       EditText edtTenSach = view.findViewById(R.id.edtTenSach);
       EditText edtTien = view.findViewById(R.id.edtTien);
       TextView txtMaSach = view.findViewById(R.id.txtMaSach);
       Spinner spnLoaiSach = view.findViewById(R.id.spnLoaiSach);

       txtMaSach.setText("Ma Sach" + sach.getMaSach());
       edtTenSach.setText(sach.getTenSach());
       edtTien.setText(String.valueOf(sach.getGiaThue()));

       SimpleAdapter simpleAdapter = new SimpleAdapter(
               context,
               listHM,
               android.R.layout.simple_list_item_1,
               new String[]{"tenLoai"},
               new int[]{android.R.id.text1}
       );
       spnLoaiSach.setAdapter(simpleAdapter);
       int index = 0;
       int position = -1;
       for (HashMap<String,Object> item: listHM){
           if ((int)item.get("maLoai") == sach.getMaLoai()){
               position = index;
           }
           index ++;

       }
       spnLoaiSach.setSelection(position);
       builder.setNegativeButton("Cap nhat", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {
               String tenSach = edtTenSach.getText().toString();
               int tien =Integer.parseInt(edtTien.getText().toString()) ;

               HashMap<String,Object> hs = (HashMap<String, Object>) spnLoaiSach.getSelectedItem();
               int maLoai = (int) hs.get("maLoai");
               boolean check = sachhDDAO.capNhatThongTinSach(sach.getMaSach(),tenSach,tien,maLoai);

               if (check){
                   Toast.makeText(context, "Cap nhat thanh cong", Toast.LENGTH_SHORT).show();
                   loadData();
               }else{
                   Toast.makeText(context, "Cap Nhat that bai", Toast.LENGTH_SHORT).show();
               }
           }
       });
       builder.setPositiveButton("Huy", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {

           }
       });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
   }
private  void loadData(){
        list.clear();
        list = sachhDDAO.getDSDauSach();
        notifyDataSetChanged();
}
}
