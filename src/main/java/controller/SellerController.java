package controller;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.SellerDao;
import model.Seller;
import services.Services;

/**
 * Servlet implementation class SellerController
 */
@WebServlet("/SellerController")
public class SellerController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SellerController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		System.out.println(action);
		if(action.equalsIgnoreCase("register")) {
			Seller s = new Seller();
			s.setName(request.getParameter("name"));
			s.setContact(Long.parseLong(request.getParameter("contact")));
			s.setAddress(request.getParameter("address"));
			s.setEmail(request.getParameter("email"));
			s.setPassword(request.getParameter("password"));
			SellerDao.insertSeller(s);
			request.setAttribute("msg", "data inesrted successsfully");
			request.getRequestDispatcher("seller-login.jsp").forward(request, response);
		}
		else if(action.equalsIgnoreCase("login")) {
			Seller s = new Seller();
			s.setEmail(request.getParameter("email"));
			s.setPassword(request.getParameter("password"));
			Seller s1 = SellerDao.checkSellerLogin(s);
			if(s1 == null) {
				request.setAttribute("validate", "Email or password is incorrect");
				request.getRequestDispatcher("seller-login.jsp").forward(request, response);
			}
			else {
				HttpSession session = request.getSession();
				session.setAttribute("data", s1);
				request.getRequestDispatcher("seller-index.jsp").forward(request, response);
			}
		}
		else if(action.equalsIgnoreCase("update")) {
			Seller s = new Seller();
			s.setId(Integer.parseInt(request.getParameter("id")));
			s.setName(request.getParameter("name"));
			s.setContact(Long.parseLong(request.getParameter("contact")));
			s.setAddress(request.getParameter("address"));
			s.setEmail(request.getParameter("email"));
			SellerDao.updateSeller(s);
			HttpSession session = request.getSession();
			session.setAttribute("data", s);
			request.getRequestDispatcher("seller-profile.jsp").forward(request, response);
		}
		else if(action.equalsIgnoreCase("change password")) {
			Seller s = new Seller();
			int id = Integer.parseInt(request.getParameter("id"));
			String pass = request.getParameter("op");
			String np = request.getParameter("np");
			String cnp = request.getParameter("cnp");
			boolean flag = SellerDao.checkOldPassword(id, pass);
			System.out.println(flag);
			if(flag == true) {
				if(np.equals(cnp)) {
					SellerDao.changeSellerPassword(np, id);
					response.sendRedirect("seller-index.jsp");
				}
				else {
					request.setAttribute("msg1", "new password and confirm new password not matched");
					request.getRequestDispatcher("seller-change-password.jsp").forward(request, response);
				}
			}
			else {
				request.setAttribute("msg", "old password not correct");
				request.getRequestDispatcher("seller-change-password.jsp").forward(request, response);
			}
		}
		else if(action.equalsIgnoreCase("get otp")) {
			String email = request.getParameter("email");
			boolean flag = SellerDao.checkEmail(email);
			if(flag == true) {
				Services s=new Services();
				Random r=new Random();
				int num=r.nextInt(9999);
				System.out.println(num);
				s.sendMail(email,num);
				request.setAttribute("email", email);
				request.setAttribute("otp", num);
				request.getRequestDispatcher("seller-verify-otp.jsp").forward(request, response);
			}
			else {
				request.setAttribute("msg", "email id not exist");
				request.getRequestDispatcher("seller-forgot-password.jsp").forward(request, response);
			}
		}
		else if(action.equalsIgnoreCase("verify")) {
			String email = request.getParameter("email");
			int otp1 = Integer.parseInt(request.getParameter("otp1"));
			int otp2 = Integer.parseInt(request.getParameter("otp2"));
			System.out.println(email+otp1+otp2);
			if(otp1==otp2) {
				request.setAttribute("email", email);
				request.getRequestDispatcher("seller-new-password.jsp").forward(request, response);
			}
			else {
				request.setAttribute("otpnot", "OTP not matched");
				request.getRequestDispatcher("seller-verify-otp.jsp").forward(request, response);
			}
		}
		else if(action.equalsIgnoreCase("update password")) {
			String email = request.getParameter("email");
			String np = request.getParameter("np");
			String cnp = request.getParameter("cnp");
			if(np.equals(cnp)) {
				SellerDao.updateSellerPassword(email, np);
				response.sendRedirect("seller-login.jsp");
			}
			else {
				request.setAttribute("validate", "new password and confirm new password not matched");
				request.getRequestDispatcher("seller-new-password.jsp").forward(request, response);
			}
		}
	}

}
