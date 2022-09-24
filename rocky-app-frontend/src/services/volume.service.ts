import { AxiosResponse } from "axios";
import axiosApiInstance from "../axios-instance";
import { addSortsForSearch } from "../utils/helpers/global.helper";
import {
  IApiResponse,
  IPagination,
} from "../utils/interfaces/global.interface";
import {
  IVolume,
  IVolumeCriteriaSearch,
} from "../utils/interfaces/volume.interface";

class VolumeService {
  private endPointPath: string = "/volume";

  findById = (volumeId: number): Promise<AxiosResponse<IVolume>> => {
    return axiosApiInstance.get<IVolume>(
      `${this.endPointPath}/${volumeId}`
    );
  };

  create = (volume: IVolume): Promise<AxiosResponse<IVolume>> => {
    return axiosApiInstance.post<IVolume>(this.endPointPath, volume);
  };

  update = (
    id: number,
    volume: IVolume
  ): Promise<AxiosResponse<IVolume>> => {
    return axiosApiInstance.put<IVolume>(
      `${this.endPointPath}/${id}`,
      volume
    );
  };

  delete = (id: number) => {
    return axiosApiInstance.delete(`${this.endPointPath}/${id}`);
  };

  search = (
    data: IVolumeCriteriaSearch,
    pagination?: IPagination
  ): Promise<AxiosResponse<IApiResponse<IVolume>>> => {
    return axiosApiInstance.post<IApiResponse<IVolume>>(
      `${this.endPointPath}/search?page=${pagination?.page}&size=${
        pagination?.size
      }&sort=${addSortsForSearch(pagination?.sorts)}`,
      data
    );
  };
}
export const volumeService = new VolumeService();
