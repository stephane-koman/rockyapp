import { Col, Form, Input, InputNumber, Modal, Row, Select } from "antd";
import { useEffect, useState, useTransition } from "react";
import { useTranslation } from "react-i18next";
import ModalFooterActions from "../../../components/ModalFooterActions/ModalFooterActions";
import { volumeService } from "../../../services/volume.service";
import { EActionType, EAgainType } from "../../../utils/enums/global.enum";
import { EMesure } from "../../../utils/enums/mesure.enum";
import { IVolume } from "../../../utils/interfaces/volume.interface";
import "./Volume.modal.scss";

interface IProps {
  isOpen: boolean;
  type?: EActionType;
  volume?: IVolume;
  onClose: (change?: boolean) => void;
}

export const VolumeModal = ({
  isOpen,
  type,
  volume,
  onClose,
}: IProps) => {
  const { t } = useTranslation();
  const [isPending, startTransition] = useTransition();
  const [form] = Form.useForm();
  const [addAgain, setAddAgain] = useState<boolean>(false);

  useEffect(() => {
    if (isOpen && volume) {
      form.setFieldsValue(volume);
    }

    if (isOpen && !volume) {
      form.resetFields();
    }
  }, [volume, isOpen, form]);

  const onCloseHandler = (change?: boolean) => {
    form.resetFields();
    if (!addAgain) {
      onClose(change);
    } 
    setAddAgain(false);
  };

  const onCreateVolume = (data: IVolume) => {
    startTransition(() => {
      volumeService
        .create(data)
        .then((_) => {
          onCloseHandler(true);
        })
        .catch((err: any) => console.error(err));
    });
  };

  const onUpdateVolume = (data: IVolume) => {
    if (volume) {
      startTransition(() => {
        volumeService
          .update(volume.id, data)
          .then((_) => {
            onCloseHandler(true);
          })
          .catch((err: any) => console.error(err));
      });
    }
  };

  const onFinishHandler = (values: any) => {
    if (volume) onUpdateVolume(values);
    else onCreateVolume(values);
  };

  const onCancelHandler = () => {
    setAddAgain(false);
    onClose();
  }

  return (
    <Modal
      className="Volume"
      open={isOpen}
      destroyOnClose
      width={1000}
      title={t(`volume.${type}_volume`)}
      onCancel={() => onCancelHandler()}
      onOk={form.submit}
      footer={
        <ModalFooterActions
          again={
            type === EActionType.CREATE
              ? {
                  text: t("common.volume"),
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
        name="volume-ref"
        scrollToFirstError
        labelCol={{
          span: 6,
        }}
        wrapperCol={{
          span: 18,
        }}
        initialValues={{
          id: "",
          quantity: null,
          mesure: EMesure.ML,
          description: "",
        }}
        onFinish={onFinishHandler}
      >
        <Input name="id" type="hidden" />

        <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
          <Col className="gutter-row" span={12}>
            <Form.Item
              name="quantity"
              label={t("common.quantity")}
              rules={[
                {
                  required: true,
                  type: "number",
                  message: t("required.quantity"),
                },
              ]}
            >
              <InputNumber
                style={{ width: "100%" }}
                disabled={type === EActionType.READ}
              />
            </Form.Item>
          </Col>
          <Col className="gutter-row" span={12}>
            <Form.Item
              name="mesure"
              label={t("common.mesure")}
              rules={[
                {
                  required: true,
                  message: t("required.mesure"),
                },
              ]}
            >
              <Select
                style={{ width: "100%" }}
                placeholder=""
                disabled={type === EActionType.READ}
                options={Object.values(EMesure)?.map((m: string) => ({
                  label: m,
                  value: m,
                }))}
              />
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
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

export default VolumeModal;
