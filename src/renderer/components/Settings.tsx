import React from 'react'
import {useAppContext} from "../AppContext"
import {Drawer, Form, Switch} from "antd";
declare type EventType = React.KeyboardEvent<HTMLDivElement> | React.MouseEvent<HTMLDivElement | HTMLButtonElement>;

export const Settings = (): JSX.Element => {
  const {darkMode, setDarkMode, settingsDrawerOpen, setSettingsDrawerOpen} = useAppContext()

  const [form] = Form.useForm()
  const layout = {
    labelCol: {span: 8},
    wrapperCol: {span: 16},
  }

  function formItems(): JSX.Element {
    return (
      <>
        <Form.Item label="Dark Mode">
          <Switch
            checkedChildren="On"
            unCheckedChildren="Off"
            onChange={setDarkMode}
            checked={darkMode}/>
        </Form.Item>
      </>
    )
  }

  console.log(`XXX settingsDrawerOpen ${settingsDrawerOpen}, darkMode: ${darkMode}`)
  return (
    <Drawer
      title={'Settings'}
      width={550}
      placement="right"
      closable={true}
      visible={settingsDrawerOpen}
      onClose={(_: EventType) => setSettingsDrawerOpen(false)}
    >
      <Form
        form={form}
        size={'large'}
        {...layout}>
        {formItems()}
      </Form>
    </Drawer>
  )
}
