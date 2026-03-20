package com.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.conn.DBConnect;
import com.dao.DAOSearch;
import com.entity.viewlist;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SearchServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String q = request.getParameter("q");
		String brand = request.getParameter("brand");
		String category = request.getParameter("category");
		String minPriceStr = request.getParameter("minPrice");
		String maxPriceStr = request.getParameter("maxPrice");

		if (q == null) q = "";

		int minPrice = 0;
		int maxPrice = Integer.MAX_VALUE;
		try {
			if (minPriceStr != null && !minPriceStr.trim().isEmpty()) {
				minPrice = Integer.parseInt(minPriceStr.trim());
			}
		} catch (NumberFormatException e) {
			minPrice = 0;
		}
		try {
			if (maxPriceStr != null && !maxPriceStr.trim().isEmpty()) {
				maxPrice = Integer.parseInt(maxPriceStr.trim());
			}
		} catch (NumberFormatException e) {
			maxPrice = Integer.MAX_VALUE;
		}

		List<viewlist> results = null;
		try {
			DAOSearch dao = new DAOSearch(DBConnect.getConn());
			results = dao.searchProducts(q, brand, category, minPrice, maxPrice);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("fail.jsp");
			return;
		}

		request.setAttribute("searchResults", results);
		request.setAttribute("query", q);

		// Detect user type from cookies to forward to the correct navbar variant
		String forward = "search_results.jsp";
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if ("tname".equals(c.getName()) && c.getValue() != null && !c.getValue().isEmpty()) {
					forward = "search_resultsa.jsp";
					break;
				}
				if ("cname".equals(c.getName()) && c.getValue() != null && !c.getValue().isEmpty()) {
					forward = "search_resultsc.jsp";
				}
			}
		}

		RequestDispatcher rd = request.getRequestDispatcher(forward);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
