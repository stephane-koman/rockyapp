import axiosApiInstance from "../axios-instance";
import { JWT_TOKEN } from "../utils/constants/global.constant";
import { IStatus } from "../utils/interfaces/global.interface";

class GlobalService {
  login = (data: any) => {
    return axiosApiInstance.post(
      "/auth",
      new URLSearchParams({
        username: data?.email,
        password: data?.password,
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

  updateStatus = (id: any, status: IStatus, endPointPath: string) => {
    return axiosApiInstance.put(`/${endPointPath}/status/${id}`, status);
  };

  getVersion = () => {
    return axiosApiInstance.get("/version");
  };
}

export const globalService = new GlobalService();