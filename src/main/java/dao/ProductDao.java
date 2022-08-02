package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dbConnection.DBConnection;
import model.Product;

public class ProductDao {
	public static void insertProduct(Product p) {
		try {
			Connection conn = DBConnection.createConnection();
			String sql="insert into product(sid,image,pname,pcategory,pprice) values(?,?,?,?,?)";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, p.getSid());
			pst.setString(2, p.getImage());
			pst.setString(3, p.getPname());
			pst.setString(4, p.getPcategory());
			pst.setDouble(5, p.getPprice());
			pst.executeUpdate();
			System.out.println("product uploaded");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static List<Product> getProductListBySid(int id){
		List<Product> list = new ArrayList<Product>();
		try {
			Connection conn = DBConnection.createConnection();
			String sql="select * from product where sid=?";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				Product p = new Product();
				p.setPid(rs.getInt("pid"));
				p.setSid(rs.getInt("sid"));
				p.setImage(rs.getString("image"));
				p.setPname(rs.getString("pname"));
				p.setPcategory(rs.getString("pcategory"));
				p.setPprice(rs.getDouble("pprice"));
				list.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public static Product getProductById(int id) {
		Product p = null;
		try {
			Connection conn = DBConnection.createConnection();
			String sql="select * from product where pid=?";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				p = new Product();
				p.setPid(rs.getInt("pid"));
				p.setSid(rs.getInt("sid"));
				p.setImage(rs.getString("image"));
				p.setPname(rs.getString("pname"));
				p.setPcategory(rs.getString("pcategory"));
				p.setPprice(rs.getDouble("pprice"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
}
