import { red } from "@ant-design/colors";
import { WarningOutlined } from "@ant-design/icons";
import {
  Col,
  Divider,
  Form,
  Input,
  List,
  Modal,
  Row,
  Select,
  Transfer,
} from "antd";
import { TFunction } from "i18next";
import { useEffect, useState, useTransition } from "react";
import { useTranslation } from "react-i18next";
import ModalFooterActions from "../../../components/ModalFooterActions/ModalFooterActions";
import { userService } from "../../../services/user.service";
import { EActionType, EAgainType } from "../../../utils/enums/global.enum";
import { globalFilterOption } from "../../../utils/helpers/global.helper";
import { IPermission } from "../../../utils/interfaces/permission.interface";
import { ISimpleRole } from "../../../utils/interfaces/role.interface";
import {
  ISimpleUser,
  IUserCrea,
} from "../../../utils/interfaces/user.interface";

interface IProps {
  isOpen: boolean;
  type?: EActionType;
  user?: ISimpleUser;
  roleList?: ISimpleRole[];
  permissionList?: IPermission[];
  onClose: (change?: boolean) => void;
}

export const UserModal = ({
  isOpen,
  type,
  user,
  roleList,
  permissionList,
  onClose,
}: IProps) => {
  const { t } = useTranslation();
  const [isPending, startTransition] = useTransition();
  const startTransfertTransition = useTransition()[1];
  const [form] = Form.useForm();
  const [addAgain, setAddAgain] = useState<boolean>(false);
  const [errors, setErrors] = useState<any[]>([]);
  const [permissionsDataSource, setPermissionsDataSource] = useState<
    any[] | undefined
  >([]);
  const [permissionsTargetKeys, setPermissionsTargetKeys] = useState<string[]>(
    []
  );

  useEffect(() => {
    if (isOpen && user) {
      //form.setFieldsValue(user);

      initDataSource();

      getUserById();
    }

    if (isOpen && !user) {
      form.resetFields();

      initDataSource();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [user, isOpen, form]);

  const getUserById = () => {
    if (user) {
      startTransfertTransition(() => {
        userService
          .findById(user.id)
          .then((res) => {
            const data = res?.data;
            form.setFieldsValue({
              ...user,
              roleList: data.roleList,
            });
            setPermissionsTargetKeys(data.permissionList);
          })
          .catch((err: any) => console.error(err));
      });
    }
  };

  const initDataSource = () => {
    startTransfertTransition(() => {
      setPermissionsDataSource(permissionList);
    });
  };

  const onCloseHandler = (change?: boolean) => {
    form.resetFields();
    setErrors([]);
    if (!addAgain) {
      onClose(change);
    } else {
      initDataSource();
    }
    setPermissionsTargetKeys([]);
    setAddAgain(false);
  };

  const onCreateUser = (data: IUserCrea) => {
    startTransition(() => {
      data.permissionList = permissionsTargetKeys;
      userService
        .create(data)
        .then((_) => {
          onCloseHandler(true);
        })
        .catch((err: any) => console.error(err));
    });
  };

  const onUpdateUser = (data: IUserCrea) => {
    if (user) {
      startTransition(() => {
        data.permissionList = permissionsTargetKeys;
        userService
          .update(user?.id, data)
          .then((_) => {
            onCloseHandler(true);
          })
          .catch((err: any) => console.error(err));
      });
    }
  };

  const onFinishHandler = (values: IUserCrea) => {
    if (user) onUpdateUser(values);
    else onCreateUser(values);
  };

  const filterOption = (inputValue: string, option: any) =>
    globalFilterOption(inputValue, option, "name");

  const permissionsChangeHandler = (targetKeysChange: any) => {
    setPermissionsTargetKeys(targetKeysChange);
  };

  const onCancelHandler = () => {
    setPermissionsTargetKeys([]);
    setAddAgain(false);
    onClose();
  };

  return (
    <Modal
      open={isOpen}
      destroyOnClose
      title={t(`user.${type}_user`)}
      width={1000}
      onCancel={() => onCancelHandler()}
      onOk={form.submit}
      footer={
        <ModalFooterActions
          again={type !== EActionType.READ ? {
            text: t("common.user"),
            type: EAgainType.Un,
          } : undefined}
          type={type}
          loading={isPending}
          addAgain={addAgain}
          onAddAgain={(check: boolean) => setAddAgain(check)}
          onClose={onCloseHandler}
          onSubmit={form.submit}
        />
      }
    >
      <Form
        form={form}
        name="user-ref"
        scrollToFirstError
        labelCol={{
          span: 6,
        }}
        wrapperCol={{
          span: 18,
        }}
        initialValues={{
          id: "",
          name: "",
          username: "",
          password: null,
          passwordConfirm: null,
          email: "",
          roleList: [],
          permissionList: [],
        }}
        onFinish={onFinishHandler}
      >
        {Object.keys(errors).length > 0 && (
          <List
            dataSource={Object.values(errors)}
            renderItem={(item) => (
              <List.Item>
                <List.Item.Meta
                  className="text-center"
                  description={
                    <span style={{ color: red.primary }}>
                      <WarningOutlined style={{ marginRight: "16px" }} />
                      {item}
                    </span>
                  }
                />
              </List.Item>
            )}
          />
        )}
        <Input name="id" type="hidden" />

        <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
          <Col className="gutter-row" span={12}>
            <Form.Item
              name="name"
              label={t("common.name")}
              rules={[
                {
                  required: true,
                  message: t("required.name"),
                },
              ]}
            >
              <Input disabled={type === EActionType.READ} />
            </Form.Item>
          </Col>
          {type !== EActionType.CREATE && <EmailInput t={t} type={type} />}
          {type === EActionType.CREATE && <PasswordInput t={t} type={type} />}
        </Row>

        <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
          <Col className="gutter-row" span={12}>
            <Form.Item
              name="username"
              label={t("common.username")}
              rules={[
                {
                  required: true,
                  message: t("required.username"),
                },
              ]}
            >
              <Input disabled={type === EActionType.READ} />
            </Form.Item>
          </Col>

          {type !== EActionType.CREATE && (
            <RolesInput t={t} type={type} roleList={roleList} />
          )}

          {type === EActionType.CREATE && (
            <PasswordConfirmInput t={t} type={type} />
          )}
        </Row>

        {type === EActionType.CREATE && (
          <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
            <EmailInput t={t} type={type} />
            <RolesInput t={t} type={type} roleList={roleList} />
          </Row>
        )}

        <Divider />

        <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
          <Col className="gutter-row" span={24}>
            <div
              className="ant-form-item-label"
              style={{
                marginBottom: "24px",
                fontWeight: "bold",
                fontSize: "15px",
              }}
            >
              {t("common.permissions")}
            </div>

            <Transfer
              disabled={type === EActionType.READ}
              rowKey={(record) => record.name}
              titles={[
                t("common.transfert.source"),
                t("common.transfert.target"),
              ]}
              dataSource={permissionsDataSource}
              showSearch
              filterOption={filterOption}
              targetKeys={permissionsTargetKeys}
              onChange={permissionsChangeHandler}
              render={(item) => item?.name}
            />
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default UserModal;

interface IInput {
  t: any;
  type?: EActionType;
  roleList?: ISimpleRole[];
}

const RolesInput = ({ t, type, roleList }: IInput) => (
  <Col className="gutter-row" span={12}>
    <Form.Item name="roleList" label={t("common.roles")}>
      <Select
        allowClear
        autoClearSearchValue
        mode="multiple"
        style={{ width: "100%" }}
        placeholder=""
        disabled={type === EActionType.READ}
        options={roleList?.map((role: ISimpleRole) => ({
          label: role.name,
          value: role.name,
        }))}
      />
    </Form.Item>
  </Col>
);

const EmailInput = ({ t, type }: IInput) => (
  <Col className="gutter-row" span={12}>
    <Form.Item
      name="email"
      label={t("common.email")}
      rules={[
        {
          required: true,
          type: "email",
          message: t("required.email"),
        },
      ]}
    >
      <Input disabled={type === EActionType.READ} />
    </Form.Item>
  </Col>
);

const PasswordInput = ({ t, type }: IInput) => (
  <Col className="gutter-row" span={12}>
    <Form.Item
      name="password"
      label={t("common.password")}
      rules={[{ required: true, message: t("user.password_required") }]}
    >
      <Input.Password disabled={type === EActionType.READ} />
    </Form.Item>
  </Col>
);

const PasswordConfirmInput = ({ t, type }: IInput) => (
  <Col className="gutter-row" span={12}>
    <Form.Item
      name="passwordConfirm"
      label={<div>{t("common.confirm_password")}</div>}
      dependencies={["password"]}
      hasFeedback
      rules={[
        {
          required: true,
          message: t("user.confirm_password_required"),
        },
        ({ getFieldValue }) => ({
          validator(_, value) {
            if (!value || getFieldValue("password") === value) {
              return Promise.resolve();
            }
            return Promise.reject(new Error(t("user.password_not_match")));
          },
        }),
      ]}
    >
      <Input.Password disabled={type === EActionType.READ} />
    </Form.Item>
  </Col>
);
