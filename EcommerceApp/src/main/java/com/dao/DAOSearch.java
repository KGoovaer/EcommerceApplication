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
			String likeQ = "%" + q + "%";

			StringBuilder sql = new StringBuilder(
				"SELECT * FROM viewlist WHERE (Bname LIKE ? OR Cname LIKE ? OR Pname LIKE ?)");

			if (brand != null && !brand.trim().isEmpty())
				sql.append(" AND Bname = ?");

			if (category != null && !category.trim().isEmpty())
				sql.append(" AND Cname = ?");

			sql.append(" AND Pprice BETWEEN ? AND ?");

			PreparedStatement ps = conn.prepareStatement(sql.toString());

			int idx = 1;
			ps.setString(idx++, likeQ);
			ps.setString(idx++, likeQ);
			ps.setString(idx++, likeQ);

			if (brand != null && !brand.trim().isEmpty())
				ps.setString(idx++, brand.trim());

			if (category != null && !category.trim().isEmpty())
				ps.setString(idx++, category.trim());

			ps.setInt(idx++, minPrice);
			ps.setInt(idx++, maxPrice);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				v = new viewlist();
				v.setBname(rs.getString(1));
				v.setCname(rs.getString(2));
				v.setPname(rs.getString(3));
				v.setPprice(rs.getInt(4));
				v.setPquantity(rs.getInt(5));
				v.setPimage(rs.getString(6));
				list.add(v);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
