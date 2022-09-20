import axiosApiInstance from "../axios-instance";
import { JWT_TOKEN } from "../utils/constants/global.constant";

class GlobalService {
  login = (data: any) => {
    return axiosApiInstance.post(
      "/auth",
      new URLSearchParams({
        username: data?.email,
        password: data?.password
      }),
      {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
      }
    );
  };

  refreshAccessToken = async () => {
    const value: any = localStorage.getItem(JWT_TOKEN);
    const keys = JSON.parse(value);

    console.log("keys", keys);
    
    return axiosApiInstance
      .get("/refreshToken", {
        headers: {
          Authorization: `Bearer ${keys?.refresh_token}`,
          Accept: "application/json",
        },
      })
      .then((response: any) => {
        localStorage.setItem(JWT_TOKEN, JSON.stringify(response?.data));
        return response?.data?.access_token;
      });
  };

  getVersion = () => {
    return axiosApiInstance.get("/version");
  };
}

export const globalService = new GlobalService();