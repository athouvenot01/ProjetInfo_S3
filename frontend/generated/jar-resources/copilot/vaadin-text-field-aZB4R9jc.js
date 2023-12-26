import { S as o, L as t, T as r, K as e } from "./copilot-VGmjfoSi.js";
const i = [
  t.backgroundColor,
  t.borderColor,
  t.borderWidth,
  t.borderRadius,
  r.height,
  r.paddingInline,
  e.textColor,
  e.fontSize,
  e.fontWeight
], a = [
  e.textColor,
  e.fontSize,
  e.fontWeight
], s = [
  e.textColor,
  e.fontSize,
  e.fontWeight
], l = [
  e.textColor,
  e.fontSize,
  e.fontWeight
], d = {
  tagName: "vaadin-text-field",
  displayName: "Text Field",
  elements: [
    {
      selector: "vaadin-text-field::part(input-field)",
      displayName: "Input field",
      properties: i
    },
    {
      selector: "vaadin-text-field::part(label)",
      displayName: "Label",
      properties: a
    },
    {
      selector: "vaadin-text-field::part(helper-text)",
      displayName: "Helper text",
      properties: s
    },
    {
      selector: "vaadin-text-field::part(error-message)",
      displayName: "Error message",
      properties: l
    },
    {
      selector: "vaadin-text-field::part(clear-button)",
      displayName: "Clear button",
      properties: o
    }
  ]
};
export {
  d as default,
  l as errorMessageProperties,
  s as helperTextProperties,
  i as inputFieldProperties,
  a as labelProperties
};
