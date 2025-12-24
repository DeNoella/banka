import api from "../api/axios";

const UserService = {
  // Admin creates Manager
  createManager(managerData) {
    return api.post("/admin/create-manager", managerData);
  },

  // Manager creates Cashier or Client
  createUser(userData) {
    return api.post("/manager/create-user", userData);
  },

  // Get all users (if needed for displaying lists)
  getAllUsers() {
    return api.get("/users");
  },

  // Get user by ID
  getUserById(userId) {
    return api.get(`/users/${userId}`);
  },

  // Update user
  updateUser(userId, userData) {
    return api.put(`/users/${userId}`, userData);
  },

  // Delete user
  deleteUser(userId) {
    return api.delete(`/users/${userId}`);
  }
};

export default UserService;