// import React, { useState, useEffect } from "react";
// import { Drawer } from "antd";
//
// // From https://codesandbox.io/s/basic-ant-design-demo-forked-6j48o?file=/ResizableDrawer.jsx:0-1509
//
// let isResizing = null;
//
// const ResizableDrawer = ({ children, ...props }) => {
//   const [drawerWidth, setDrawerWidth] = useState(undefined);
//
//   const cbHandleMouseMove = React.useCallback(handleMousemove, []);
//   const cbHandleMouseUp = React.useCallback(handleMouseup, []);
//
//   useEffect(() => {
//     setDrawerWidth(props.width);
//     // eslint-disable-next-line react-hooks/exhaustive-deps
//   }, [props.visible]);
//
//   function handleMouseup(e) {
//     if (!isResizing) {
//       return;
//     }
//     isResizing = false;
//     document.removeEventListener("mousemove", cbHandleMouseMove);
//     document.removeEventListener("mouseup", cbHandleMouseUp);
//   }
//
//   function handleMousedown(e) {
//     e.stopPropagation();
//     e.preventDefault();
//     // we will only add listeners when needed, and remove them afterward
//     document.addEventListener("mousemove", cbHandleMouseMove);
//     document.addEventListener("mouseup", cbHandleMouseUp);
//     isResizing = true;
//   }
//
//   function handleMousemove(e) {
//     let offsetRight =
//       document.body.offsetWidth - (e.clientX - document.body.offsetLeft);
//     let minWidth = 256;
//     let maxWidth = 600;
//     if (offsetRight > minWidth && offsetRight < maxWidth) {
//       setDrawerWidth(offsetRight);
//     }
//   }
//
//   return (
//     <Drawer {...props} width={drawerWidth}>
//       <div className="sidebar-dragger" onMouseDown={handleMousedown} />
//       {children}
//     </Drawer>
//   );
// };
//
// export default ResizableDrawer;
