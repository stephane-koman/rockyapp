import { AxiosResponse } from "axios";
import axiosApiInstance from "../axios-instance";
import { addSortsForSearch } from "../utils/helpers/global.helper";
import {
  IApiResponse,
  IPagination,
} from "../utils/interfaces/global.interface";
import {
  ICustomer,
  ICustomerCriteriaSearch,
} from "../utils/interfaces/customer.interface";

class CustomerService {
  private endPointPath: string = "/customer";

  findById = (customerId: number): Promise<AxiosResponse<ICustomer>> => {
    return axiosApiInstance.get<ICustomer>(
      `${this.endPointPath}/${customerId}`
    );
  };

  create = (customer: ICustomer): Promise<AxiosResponse<ICustomer>> => {
    return axiosApiInstance.post<ICustomer>(this.endPointPath, customer);
  };

  update = (
    id: number,
    customer: ICustomer
  ): Promise<AxiosResponse<ICustomer>> => {
    return axiosApiInstance.put<ICustomer>(
      `${this.endPointPath}/${id}`,
      customer
    );
  };

  delete = (id: number) => {
    return axiosApiInstance.delete(`${this.endPointPath}/${id}`);
  };

  search = (
    data: ICustomerCriteriaSearch,
    pagination?: IPagination
  ): Promise<AxiosResponse<IApiResponse<ICustomer>>> => {
    return axiosApiInstance.post<IApiResponse<ICustomer>>(
      `${this.endPointPath}/search?page=${pagination?.page}&size=${
        pagination?.size
      }&sort=${addSortsForSearch(pagination?.sorts)}`,
      data
    );
  };
}
export const customerService = new CustomerService();
