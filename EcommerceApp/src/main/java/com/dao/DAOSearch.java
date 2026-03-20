package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.entity.viewlist;

public class DAOSearch {
	private Connection conn;

	public DAOSearch(Connection conn) {
		this.conn = conn;
	}

	public List<viewlist> searchProducts(String q, String brand, String category, int minPrice, int maxPrice) {
		List<viewlist> list = new ArrayList<viewlist>();
		viewlist v = null;

		try {
			String keyword = "%" + (q != null ? q : "") + "%";

			StringBuilder sql = new StringBuilder(
				"SELECT Bname, Cname, Pname, Pprice, Pquantity, Pimage FROM viewlist WHERE (Bname LIKE ? OR Cname LIKE ? OR Pname LIKE ?)");
			List<Object> params = new ArrayList<Object>();
			params.add(keyword);
			params.add(keyword);
			params.add(keyword);

			if (brand != null && !brand.trim().isEmpty()) {
				sql.append(" AND Bname = ?");
				params.add(brand.trim());
			}
			if (category != null && !category.trim().isEmpty()) {
				sql.append(" AND Cname = ?");
				params.add(category.trim());
			}
			sql.append(" AND Pprice BETWEEN ? AND ?");
			params.add(minPrice);
			params.add(maxPrice);

			PreparedStatement ps = conn.prepareStatement(sql.toString());
			for (int i = 0; i < params.size(); i++) {
				ps.setObject(i + 1, params.get(i));
			}

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				v = new viewlist();
				v.setBname(rs.getString("Bname"));
				v.setCname(rs.getString("Cname"));
				v.setPname(rs.getString("Pname"));
				v.setPprice(rs.getInt("Pprice"));
				v.setPquantity(rs.getInt("Pquantity"));
				v.setPimage(rs.getString("Pimage"));
				list.add(v);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
