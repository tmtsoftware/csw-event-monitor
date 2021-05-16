import {ParameterModel, ParamInfoModel} from "./EventTreeData";
import {
  BaseKey,
  booleanKey, byteArrayKey,
  byteKey, byteMatrixKey, doubleArrayKey,
  doubleKey, doubleMatrixKey, floatArrayKey,
  floatKey, floatMatrixKey, intArrayKey,
  intKey, intMatrixKey,
  Key, longArrayKey,
  longKey, longMatrixKey, shortArrayKey,
  shortKey, shortMatrixKey,
  stringKey, SystemEvent
} from "@tmtsoftware/esw-ts";

export class ParameterUtil {

  // Get the CSW Key
  static getCswArrayKey(parameterModel: ParameterModel, isMatrix: boolean): BaseKey<Key> | undefined {
    const maybeName = parameterModel?.name
    const name: string = maybeName ? maybeName : "undefined"
    const maybeType = parameterModel?.maybeArrayType

    if (maybeType) {
      switch (maybeType) {
        case 'integer':
        case 'number':
          return isMatrix ? intMatrixKey(name) : intArrayKey(name)
        case 'byte':
          return isMatrix ? byteMatrixKey(name) : byteArrayKey(name)
        case 'short':
          return isMatrix ? shortMatrixKey(name) : shortArrayKey(name)
        case 'long':
          return isMatrix ? longMatrixKey(name) : longArrayKey(name)
        case 'float':
          return isMatrix ? floatMatrixKey(name) : floatArrayKey(name)
        case 'double':
          return isMatrix ? doubleMatrixKey(name) : doubleArrayKey(name)
      }
    }
    return undefined
  }

  // Get the CSW Key
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
      switch (maybeType) {
        case 'boolean':
          return booleanKey(name)
        case 'integer':
        case 'number':
          return intKey(name)
        case 'string':
          return stringKey(name)
        case 'byte':
          return byteKey(name)
        case 'short':
          return shortKey(name)
        case 'long':
          return longKey(name)
        case 'float':
          return floatKey(name)
        case 'double':
          return doubleKey(name)
        case 'array': {
          if (parameterModel?.maybeDimensions) {
            switch (parameterModel.maybeDimensions.length) {
              case 1:
                return this.getCswArrayKey(parameterModel, false)
              case 2:
                return this.getCswArrayKey(parameterModel, true)
            }
          }
        }
        // XXX TODO add other types
      }
    } else if (maybeEnum) {
      // XXX TODO add enums
    }
    return undefined
  }

  static formatFloats(ar: Array<any>): string {
    return ar.map(d => d.toFixed(3)).join(', ')
  }

  static formatArrays(ar: Array<any>): string {
    return ar.map(a => `[${a}]`).join(', ')
  }

  static formatFloatArrays(ar: Array<any>): string {
    return ar.map(a => `[${this.formatFloats(a)}]`).join(', ')
  }

  static formatMatrixes(ar: Array<any>): string {
    return ar.map(a => `[${this.formatArrays(a)}]`).join(', ')
  }

  static formatFloatMatrices(ar: Array<any>): string {
    return ar.map(a => `[${this.formatFloatArrays(a)}]`).join(', ')
  }

  // Format the parameter values for the given key for display
  static formatValues(systemEvent: SystemEvent, cswParamKey: BaseKey<Key>): string {
    console.log(`XXX keyTag = ${cswParamKey.keyTag}, keyName = ${cswParamKey.keyName}`)
    const values = systemEvent.get(cswParamKey)?.values
    if (values && values.length > 0) {
      switch (cswParamKey.keyTag) {
        case 'IntKey': return values.join(', ')
        case 'LongKey': return values.join(', ')
        case 'ShortKey': return values.join(', ')
        case 'FloatKey': return this.formatFloats(values)
        case 'DoubleKey': return this.formatFloats(values)
        case 'ByteKey': return values.join(', ')
        case 'StringKey': return values.join(', ')
        case 'CharKey': return values.join(', ')
        case 'StructKey':
        case 'ChoiceKey': return values.join(', ') // ???
        case 'IntMatrixKey': return this.formatMatrixes(values)
        case 'ByteMatrixKey': return this.formatMatrixes(values)
        case 'LongMatrixKey': return this.formatMatrixes(values)
        case 'ShortMatrixKey': return this.formatMatrixes(values)
        case 'FloatMatrixKey': return this.formatFloatMatrices(values)
        case 'DoubleMatrixKey': return this.formatFloatMatrices(values)
        case 'IntArrayKey': return this.formatArrays(values)
        case 'ByteArrayKey': return this.formatArrays(values)
        case 'LongArrayKey': return this.formatArrays(values)
        case 'ShortArrayKey': return this.formatArrays(values)
        case 'FloatArrayKey': return this.formatFloatArrays(values)
        case 'DoubleArrayKey': return this.formatFloatArrays(values)
        case 'BooleanKey': return values.join(', ')
        case 'UTCTimeKey':
        case 'TAITimeKey':
        case 'RaDecKey':
        case 'EqCoordKey':
        case 'SolarSystemCoordKey':
        case 'MinorPlanetCoordKey':
        case 'CometCoordKey':
        case 'AltAzCoordKey':
        case 'CoordKey':
      }
      // TODO: Finish type support
      return 'unsupported'
    }
    return 'undefined'
  }

}
