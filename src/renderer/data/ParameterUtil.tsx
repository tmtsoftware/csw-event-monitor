import {ParameterModel, ParamInfoModel} from "./EventTreeData";
import {
  altAzCoordKey,
  BaseKeyType,
  booleanKey, byteArrayKey,
  byteKey, byteMatrixKey, choiceKey, cometCoordKey, coordKey, doubleArrayKey,
  doubleKey, doubleMatrixKey, EqCoord, eqCoordKey, floatArrayKey,
  floatKey, floatMatrixKey, intArrayKey,
  intKey, intMatrixKey,
  Key, longArrayKey,
  longKey, longMatrixKey, minorPlanetCoordKey, RaDec, raDecKey, shortArrayKey,
  shortKey, shortMatrixKey, solarSystemCoordKey,
  stringKey, SystemEvent, taiTimeKey, utcTimeKey
} from "@tmtsoftware/esw-ts";
import {Angle} from "./Angle";

export class ParameterUtil {

  // Get the CSW Key
  static getCswArrayKey(parameterModel: ParameterModel | undefined, isMatrix: boolean): BaseKeyType<Key> | undefined {
    const maybeName = parameterModel?.name
    const name: string = maybeName ? ParameterUtil.fixParamName(maybeName) : "undefined"
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
  static getCswKey(paramInfoModel: ParamInfoModel): BaseKeyType<Key> | undefined {
    const parameterName = ParameterUtil.fixParamName(paramInfoModel.parameterName)
    const parameterModels = paramInfoModel?.eventInfoModel.eventModel.parameterList
      .filter((p) => ParameterUtil.fixParamName(p.name) == parameterName)
    const parameterModel = (parameterModels && parameterModels.length > 0) ?
      parameterModels[0] : undefined
    const maybeName = parameterModel?.name
    const name: string = maybeName ? ParameterUtil.fixParamName(maybeName) : "undefined"
    const maybeType = parameterModel?.maybeType
    const maybeEnum = parameterModel?.maybeEnum

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
        case 'taiDate':
          return taiTimeKey(name)
        case 'utcDate':
          return utcTimeKey(name)
        case 'array': {
          if (parameterModel?.maybeDimensions && parameterModel.maybeDimensions.length == 2)
                return this.getCswArrayKey(parameterModel, true)
          return this.getCswArrayKey(parameterModel, false)
        }
        case 'raDec':
          return raDecKey(name)
        case 'eqCoord':
          return eqCoordKey(name)
        case 'solarSystemCoord':
          return solarSystemCoordKey(name)
        case 'minorPlanetCoord':
          return minorPlanetCoordKey(name)
        case 'cometCoord':
          return cometCoordKey(name)
        case 'altAzCoord':
          return altAzCoordKey(name)
        case 'coord':
          return coordKey(name)
      }
    } else if (maybeEnum) {
      return choiceKey(name, maybeEnum)
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

  static formatRaDec(raDec: RaDec): string {
    // XXX What units? epoch? Format as hms dms?
    return `ra: ${raDec.ra.toFixed(3)}, dec: ${raDec.dec.toFixed(3)}`
  }

  static formatEqCoord(eq: EqCoord): string {
    const ra = Angle.raToString(new Angle(eq.ra).toRadian())
    const dec = Angle.deToString(new Angle(eq.dec).toRadian())
    const f = eq.frame.toString()
    const c = eq.catalogName
    const pm = `${eq.pm.pmx.toFixed(3)}, ${eq.pm.pmy.toFixed(3)}`
    return `tag: ${eq.tag}, ra: ${ra}, dec: ${dec}, frame: ${f}, catalog: ${c}, pm: ${pm}`
  }

  static formatEqCoords(ar: Array<any>): string {
    return ar.map(a => `[${this.formatEqCoord(a)}]`).join(', ')
  }

  // Format the parameter values for the given key for display
  static formatValues(systemEvent: SystemEvent, cswParamKey: BaseKeyType<Key>): string {
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
        case 'ChoiceKey': return values.join(', ')
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
        case 'UTCTimeKey': return values.join(', ')
        case 'TAITimeKey': return values.join(', ')
        case 'EqCoordKey': return this.formatEqCoords(values)
        case 'SolarSystemCoordKey':
        case 'MinorPlanetCoordKey':
        case 'CometCoordKey':
        case 'AltAzCoordKey':
        case 'CoordKey':
        case 'RaDecKey':
      }
      // TODO: Finish type support
      return 'unsupported'
    }
    return 'undefined'
  }

  // XXX Replace illegal chars in param names (see also ParamsetGenerator.scala in csw-generator)
  static fixParamName(paramName: string): string {
    return paramName
      .replace('/', '|')
      .replace('[', '(')
      .replace(']', ')')
  }
}
