import { red } from "@ant-design/colors";
import { WarningOutlined } from "@ant-design/icons";
import { Col, Form, Input, List, Modal, Row } from "antd";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import ModalFooterActions from "../../../components/ModalFooterActions/ModalFooterActions";
import { userService } from "../../../services/user.service";
import { EActionType } from "../../../utils/enums/global.enum";
import { ISimpleUser } from "../../../utils/interfaces/user.interface";

interface IProps {
  isOpen: boolean;
  user?: ISimpleUser;
  onClose: () => void;
}

const UserPasswordModal = ({ isOpen, user, onClose }: IProps) => {
  const { t } = useTranslation();
  const [form] = Form.useForm();
  const [errors, setErrors] = useState<any[]>([]);

  const onFinishHandler = (values: any) => {
    if(user){
       userService
         .resetPassword(user?.id, values)
         .then((_) => {
          onCloseHandler();
         })
         .catch((error) => {
           setErrors(error);
         });
    }
   
  };

  useEffect(() => {
    if(isOpen && user){
      form.setFieldsValue((value: any) => ({
        ...value,
        id: user?.id
      }));
    }
  }, [isOpen, user]);
  
  const onCloseHandler = () => {
    form.resetFields();
    onClose();
  }

  return (
    <Modal
      open={isOpen}
      destroyOnClose
      title={t("user.init_password")}
      onCancel={() => onCloseHandler()}
      onOk={form.submit}
      footer={
        <ModalFooterActions
          type={EActionType.CREATE}
          onClose={onCloseHandler}
          onSubmit={form.submit}
        />
      }
    >
      <Form
        form={form}
        name="user-ref"
        labelCol={{
          span: 6,
        }}
        wrapperCol={{
          span: 18,
        }}
        initialValues={{
          id: "",
          password: "",
          passwordConfirm: "",
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
          <Col className="gutter-row" span={24}>
            <Form.Item
              name="password"
              label={t("common.password")}
              rules={[{ required: true, message: t("user.password_required") }]}
            >
              <Input.Password />
            </Form.Item>
          </Col>
          <Col className="gutter-row" span={24}>
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
                    return Promise.reject(
                      new Error(t("user.password_not_match"))
                    );
                  },
                }),
              ]}
            >
              <Input.Password />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default UserPasswordModal;
