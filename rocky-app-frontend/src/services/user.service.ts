import { AxiosResponse } from "axios";
import axiosApiInstance from "../axios-instance";
import { addSortsForSearch } from "../utils/helpers/global.helper";
import {
  IApiResponse,
  IPagination,
} from "../utils/interfaces/global.interface";
import { IPassword, ISimpleUser, IUser, IUserCrea, IUserCriteriaSearch } from "../utils/interfaces/user.interface";

class UserService {
  private endPointPath: string = "/user";

  getUserInfos = (): Promise<AxiosResponse<IUser>> => {
    return axiosApiInstance.get<IUser>(`${this.endPointPath}`);
  };

  findById = (userId: number): Promise<AxiosResponse<IUser>> => {
    return axiosApiInstance.get<IUser>(`${this.endPointPath}/${userId}`);
  };

  create = (user: IUserCrea): Promise<AxiosResponse<IUser>> => {
    return axiosApiInstance.post<IUser>(this.endPointPath, user);
  };

  update = (id: number, user: IUserCrea): Promise<AxiosResponse<IUser>> => {
    return axiosApiInstance.put<IUser>(`${this.endPointPath}/${id}`, user);
  };

  delete = (id: number) => {
    return axiosApiInstance.delete(`${this.endPointPath}/${id}`);
  };

  resetPassword = (
    id: number,
    data: IPassword
  ): Promise<AxiosResponse<IUser>> => {
    return axiosApiInstance.put<IUser>(
      `${this.endPointPath}/reset_password/${id}`,
      data
    );
  };

  search = (
    data: IUserCriteriaSearch,
    pagination?: IPagination
  ): Promise<AxiosResponse<IApiResponse<ISimpleUser>>> => {
    return axiosApiInstance.post<IApiResponse<ISimpleUser>>(
      `${this.endPointPath}/search?page=${pagination?.page}&size=${
        pagination?.size
      }&sort=${addSortsForSearch(pagination?.sorts)}`,
      data
    );
  };
}
export const userService = new UserService();
