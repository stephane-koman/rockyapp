import axios from "axios";
import { globalService } from "./services/global.service";
import { JWT_TOKEN } from "./utils/constants/global.constant";
import { HttpStatus } from "./utils/enums/httpStatus.enum";
import { notification } from "antd";

const AUTH_URL: string = "/auth";

const axiosApiInstance = axios.create({
  baseURL:
    process.env.REACT_APP_API_URL,
});

// Request interceptor for API calls
axiosApiInstance.interceptors.request.use(
  async (config) => {
    if (!config?.url?.includes(AUTH_URL)) {
      const value: any = localStorage.getItem(JWT_TOKEN);
      const keys = JSON.parse(value);
      config.headers = {
        Authorization: `Bearer ${keys?.access_token}`,
        Accept: "application/json",
      };
    } else {
      config.headers = {
        Accept: "application/json",
      };
    }
    return config;
  },
  (error) => {
    Promise.reject(error);
  }
);

// Response interceptor for API calls
axiosApiInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  async function (error) {
    const originalRequest = error.config;
    
    if (
      error.response.status === HttpStatus.UNAUTHORIZED &&
      !originalRequest._retry &&
      error.config.url !== AUTH_URL
    ) {
      originalRequest._retry = true;
      const access_token = await globalService.refreshAccessToken();
      axios.defaults.headers.common["Authorization"] = "Bearer " + access_token;
      return axiosApiInstance(originalRequest);
    }

    if (
      error.response.status === HttpStatus.FORBIDDEN &&
      error.config.url !== AUTH_URL
    ) {
      localStorage.clear();
      window.location.replace("/login?session=expired");
    }

    if (
      error.response.status === HttpStatus.INTERNAL_SERVER_ERROR &&
      error.config.url !== AUTH_URL
    ) {
      notification.error({
        message: "Erreur",
        description: error?.response?.data?.message,
      });
    }

    return Promise.reject(error);
  }
  
);

export default axiosApiInstance;
