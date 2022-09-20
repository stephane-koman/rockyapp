import { red } from "@ant-design/colors";
import { WarningOutlined } from "@ant-design/icons";
import { Col, Divider, Form, Input, List, Modal, Row, Transfer } from "antd";
import { useEffect, useState, useTransition } from "react";
import { useTranslation } from "react-i18next";
import ModalFooterActions from "../../../components/ModalFooterActions/ModalFooterActions";
import { roleService } from "../../../services/role.service";
import { EActionType, EAgainType } from "../../../utils/enums/global.enum";
import { globalFilterOption } from "../../../utils/helpers/global.helper";
import { IPermission } from "../../../utils/interfaces/permission.interface";
import { IRole, ISimpleRole } from "../../../utils/interfaces/role.interface";
import "./Role.modal.scss";

interface IProps {
  isOpen: boolean;
  type?: EActionType;
  role?: ISimpleRole;
  permissionList?: IPermission[];
  onClose: (change?: boolean) => void;
}

export const RoleModal = ({
  isOpen,
  type,
  role,
  permissionList,
  onClose,
}: IProps) => {
  const { t } = useTranslation();
  const [isPending, startTransition] = useTransition();
  const startTransfertTransition = useTransition()[1];
  const [form] = Form.useForm();
  const [addAgain, setAddAgain] = useState<boolean>(false);
  const [errors, setErrors] = useState<any[]>([]);
  const [dataSource, setDataSource] = useState<any[] | undefined>([]);
  const [targetKeys, setTargetKeys] = useState<string[]>([]);

  useEffect(() => {
    if (isOpen && role) {
      form.setFieldsValue(role);

      initDataSource();
      getRoleById();
    }

    if (isOpen && !role) {
      form.resetFields();
      initDataSource();
    }
  }, [role, isOpen, permissionList, form]);

  const getRoleById = () => {
    if (role) {
      startTransfertTransition(() => {
        roleService
          .findById(role.id)
          .then((res) => {
            const data = res?.data;
            setTargetKeys(data.permissionList.map((p: string) => p));
          })
          .catch((err: any) => console.error(err));
      });
    }
  };

  const initDataSource = () => {
    startTransfertTransition(() => {
      setDataSource(permissionList);
    });
  };

  useEffect(() => {
    console.log("addAgain", addAgain);
    
  }, [addAgain]);
  

  const onCloseHandler = (change?: boolean) => {
    form.resetFields();
    setErrors([]);
    if (!addAgain) {
      onClose(change);
    } else {
      initDataSource();
    }
    setTargetKeys([]);
    setAddAgain(false);
  };

  const onUpdate = (resp: any) => {
    if (resp?.data?.errors) {
      setErrors(resp?.data?.errors);
    } else {
      onCloseHandler(true);
    }
  };

  const onCreateRole = (data: IRole) => {
    startTransition(() => {
      data.permissionList = targetKeys;
      roleService
        .create(data)
        .then((resp: any) => {
          onUpdate(resp);
        })
        .catch((err: any) => console.error(err));
    });
  };

  const onUpdateRole = (data: IRole) => {
    if (role) {
      startTransition(() => {
        data.permissionList = targetKeys;
        roleService
          .update(role.id, data)
          .then((resp: any) => {
            onUpdate(resp);
          })
          .catch((err: any) => console.error(err));
      });
    }
  };

  const onFinishHandler = (values: any) => {
    if (role) onUpdateRole(values);
    else onCreateRole(values);
  };

  const filterOption = (inputValue: any, option: any) =>
    globalFilterOption(inputValue, option, "name");

  const onChangeHandler = (targetKeysChange: any) => {
    setTargetKeys(targetKeysChange);
  };

  const onCancelHandler = () => {
    setTargetKeys([]);
    setAddAgain(false);
    onClose();
  }

  return (
    <Modal
      className="Role"
      open={isOpen}
      destroyOnClose
      width={1000}
      title={t(`role.${type}_role`)}
      onCancel={() => onCancelHandler()}
      onOk={form.submit}
      footer={
        <ModalFooterActions
          again={{
            text: t("common.role"),
            type: EAgainType.Un,
          }}
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
        name="role-ref"
        labelCol={{
          span: 6,
        }}
        wrapperCol={{
          span: 18,
        }}
        initialValues={{
          id: "",
          name: "",
          description: "",
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
              className="mb-0"
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
          <Col className="gutter-row" span={12}>
            <Form.Item
              className="mb-0"
              name="description"
              label={t("common.description")}
            >
              <Input disabled={type === EActionType.READ} />
            </Form.Item>
          </Col>
        </Row>

        <Divider />

        <Transfer
          disabled={type === EActionType.READ}
          rowKey={(record) => record.name}
          dataSource={dataSource}
          showSearch
          filterOption={filterOption}
          targetKeys={targetKeys}
          onChange={onChangeHandler}
          render={(item) => item?.name}
        />
      </Form>
    </Modal>
  );
};

export default RoleModal;
