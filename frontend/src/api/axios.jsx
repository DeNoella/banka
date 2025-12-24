import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/my-app/api",  
});

// Request interceptor - Add token to all requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor - Handle errors globally
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response) {
      // Server responded with error status
      switch (error.response.status) {
        case 401:
          // Unauthorized - token expired or invalid
          console.log("Unauthorized - redirecting to login");
          localStorage.clear();
          window.location.href = "/login";
          break;
        
        case 403:
          // Forbidden - user doesn't have permission
          console.log("Forbidden - insufficient permissions");
          break;
        
        case 404:
          // Not found
          console.log("Resource not found");
          break;
        
        case 500:
          // Server error
          console.log("Server error");
          break;
        
        default:
          console.log("API Error:", error.response.data);
      }
    } else if (error.request) {
      // Request made but no response received
      console.log("Network error - no response from server");
    } else {
      // Error setting up request
      console.log("Error:", error.message);
    }
    
    return Promise.reject(error);
  }
);

export const createManager = (managerData) => {
  return api.post("/admin/create-manager", managerData);
};

// new: getAllUsers with page & size
export const getAllUsers = (page = 0, size = 10) => {
  // backend expects ?page=..&size=..
  return api.get(`/admin/all?page=${page}&size=${size}`);
};

export default api;