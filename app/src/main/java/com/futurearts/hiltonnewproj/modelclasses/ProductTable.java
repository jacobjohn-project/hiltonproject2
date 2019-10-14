package com.futurearts.hiltonnewproj.modelclasses;

public class ProductTable {
    public String PO_Num,PO_Part;
    public int PO_Qty;
    public String PO_Rec_date,PO_Mv_date,PO_Loc_from,PO_Loc_to,PO_sign_by;
    public Boolean Check_1,Check_2;
    public String PO_image;

    public ProductTable() {
    }

    public ProductTable(String PO_Num, String PO_Part, int PO_Qty, String PO_Rec_date, String PO_Mv_date, String PO_Loc_from, String PO_Loc_to, String PO_sign_by, Boolean check_1, Boolean check_2) {
        this.PO_Num = PO_Num;
        this.PO_Part = PO_Part;
        this.PO_Qty = PO_Qty;
        this.PO_Rec_date = PO_Rec_date;
        this.PO_Mv_date = PO_Mv_date;
        this.PO_Loc_from = PO_Loc_from;
        this.PO_Loc_to = PO_Loc_to;
        this.PO_sign_by = PO_sign_by;
        Check_1 = check_1;
        Check_2 = check_2;
    }

    public ProductTable(String PO_Num, String PO_Part, int PO_Qty, String PO_Rec_date, String PO_Mv_date, String PO_Loc_from, String PO_Loc_to, String PO_sign_by, Boolean check_1, Boolean check_2, String PO_image) {
        this.PO_Num = PO_Num;
        this.PO_Part = PO_Part;
        this.PO_Qty = PO_Qty;
        this.PO_Rec_date = PO_Rec_date;
        this.PO_Mv_date = PO_Mv_date;
        this.PO_Loc_from = PO_Loc_from;
        this.PO_Loc_to = PO_Loc_to;
        this.PO_sign_by = PO_sign_by;
        Check_1 = check_1;
        Check_2 = check_2;
        this.PO_image = PO_image;
    }

    /*public void setPO_image(String PO_image) {
        this.PO_image = PO_image;
    }*/
}
