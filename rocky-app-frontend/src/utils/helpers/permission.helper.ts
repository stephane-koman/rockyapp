import { EActionType } from "../enums/global.enum";

export const getUserOnePermissionFromList = (permissions: string[], type: string | EActionType) => {
    return permissions.find((p: string) =>
      p?.toLowerCase().includes(type?.toString().toLowerCase())
    );
};

export const getUserManyPermissionsFromList = (permissions: string[], type: string | EActionType) => {
  const permissionList: string[] = permissions.filter((p: string) =>
    p?.toLowerCase().includes(type?.toString().toLowerCase())
  );
  return permissionList || [];
};