import {ParamInfoModel} from "./EventTreeData";
import {
  BaseKey,
  booleanKey,
  byteKey,
  doubleKey,
  floatKey,
  intKey,
  Key,
  longKey,
  shortKey,
  stringKey
} from "@tmtsoftware/esw-ts";

export class ParameterUtil {

  // Get the CSW Key
  // static getCswKey = (paramInfoModel: ParamInfoModel) => {
  static getCswKey(paramInfoModel: ParamInfoModel): BaseKey<Key> | undefined {
    const parameterModels = paramInfoModel?.eventInfoModel.eventModel.parameterList
      .filter((p) => p.name == paramInfoModel.parameterName)
    const parameterModel = (parameterModels && parameterModels.length > 0) ?
      parameterModels[0] : undefined
    const maybeName = parameterModel?.name
    const name: string = maybeName ? maybeName : "undefined"
    const maybeType = parameterModel?.maybeType
    const maybeEnum = parameterModel?.maybeEnum

    // array,
    //   struct,
    //   boolean,
    //   integer,
    //   number, // deprecated
    //   string,
    //   byte,
    //   short,
    //   long,
    //   float,
    //   double,
    //   taiDate,
    //   utcDate,
    //   raDec,
    //   eqCoord,
    //   solarSystemCoord,
    //   minorPlanetCoord,
    //   cometCoord,
    //   altAzCoord,
    //   coord

    if (maybeType) {
      switch(maybeType) {
        case 'boolean': return booleanKey(name)
        case 'integer': return intKey(name)
        case 'number': return intKey(name)
        case 'string': return stringKey(name)
        case 'byte': return byteKey(name)
        case 'short': return shortKey(name)
        case 'long': return longKey(name)
        case 'float': return floatKey(name)
        case 'double': return doubleKey(name)
        // XXX TODO add other types
      }
    } else if (maybeEnum) {
      // XXX TODO add enums
    }
    return undefined
  }
}
