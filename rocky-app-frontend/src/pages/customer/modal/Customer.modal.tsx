import { Col, Form, Input, Modal, Row } from "antd";
import { useEffect, useState, useTransition } from "react";
import { useTranslation } from "react-i18next";
import ModalFooterActions from "../../../components/ModalFooterActions/ModalFooterActions";
import { customerService } from "../../../services/customer.service";
import { EActionType, EAgainType } from "../../../utils/enums/global.enum";
import { ICustomer } from "../../../utils/interfaces/customer.interface";
import "./Customer.modal.scss";

interface IProps {
  isOpen: boolean;
  type?: EActionType;
  customer?: ICustomer;
  onClose: (change?: boolean) => void;
}

export const CustomerModal = ({
  isOpen,
  type,
  customer,
  onClose,
}: IProps) => {
  const { t } = useTranslation();
  const [isPending, startTransition] = useTransition();
  const [form] = Form.useForm();
  const [addAgain, setAddAgain] = useState<boolean>(false);

  useEffect(() => {
    if (isOpen && customer) {
      form.setFieldsValue(customer);
    }

    if (isOpen && !customer) {
      form.resetFields();
    }
  }, [customer, isOpen, form]);

  const onCloseHandler = (change?: boolean) => {
    form.resetFields();
    if (!addAgain) {
      onClose(change);
    } 
    setAddAgain(false);
  };

  const onCreateCustomer = (data: ICustomer) => {
    startTransition(() => {
      customerService
        .create(data)
        .then((_) => {
          onCloseHandler(true);
        })
        .catch((err: any) => console.error(err));
    });
  };

  const onUpdateCustomer = (data: ICustomer) => {
    if (customer) {
      startTransition(() => {
        customerService
          .update(customer.id, data)
          .then((_) => {
            onCloseHandler(true);
          })
          .catch((err: any) => console.error(err));
      });
    }
  };

  const onFinishHandler = (values: any) => {
    if (customer) onUpdateCustomer(values);
    else onCreateCustomer(values);
  };

  const onCancelHandler = () => {
    setAddAgain(false);
    onClose();
  }

  return (
    <Modal
      className="Customer"
      open={isOpen}
      destroyOnClose
      width={1000}
      title={t(`customer.${type}_customer`)}
      onCancel={() => onCancelHandler()}
      onOk={form.submit}
      footer={
        <ModalFooterActions
          again={
            type === EActionType.CREATE
              ? {
                  text: t("common.customer"),
                  type: EAgainType.Un,
                }
              : undefined
          }
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
        name="customer-ref"
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
          email: "",
          mobile: "",
          fixe: "",
          address: "",
          description: "",
        }}
        onFinish={onFinishHandler}
      >
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
          <Col className="gutter-row" span={12}>
            <Form.Item
              name="email"
              label={t("common.email")}
              rules={[
                {
                  required: false,
                  type: "email",
                },
              ]}
            >
              <Input disabled={type === EActionType.READ} />
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
          <Col className="gutter-row" span={12}>
            <Form.Item name="mobile" label={t("common.mobile")}>
              <Input disabled={type === EActionType.READ} />
            </Form.Item>
          </Col>
          <Col className="gutter-row" span={12}>
            <Form.Item name="fixe" label={t("common.fixe")}>
              <Input disabled={type === EActionType.READ} />
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
          <Col className="gutter-row" span={12}>
            <Form.Item name="address" label={t("common.address")}>
              <Input.TextArea disabled={type === EActionType.READ} />
            </Form.Item>
          </Col>
          <Col className="gutter-row" span={12}>
            <Form.Item name="description" label={t("common.description")}>
              <Input.TextArea disabled={type === EActionType.READ} />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default CustomerModal;
