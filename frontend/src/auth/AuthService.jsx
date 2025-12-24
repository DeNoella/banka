import api from "../api/axios";

const AuthService = {
  requestOtp(email) {
    console.log('Requesting OTP for:', email);
    return api.post("/auth/login/request", { email });
  },

  verifyOtp(email, otp) {
    console.log('Verifying OTP:', { email, otp, otpLength: otp.length });
    return api.post("/auth/login/verify", {
      email,
      otp,
    });
  },

  logout() {
    localStorage.clear();
  },

  getRole() {
    return localStorage.getItem("role");
  },

  isAuthenticated() {
    return !!localStorage.getItem("token");
  },
};

export default AuthService;