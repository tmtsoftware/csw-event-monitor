import type {ParameterModel, ParamInfoModel} from "./EventTreeData";
import {
  AltAzCoord,
  altAzCoordKey, Angle,
  BaseKeyType,
  booleanKey, byteArrayKey,
  byteKey, byteMatrixKey, choiceKey, CometCoord, cometCoordKey, coordKey, doubleArrayKey,
  doubleKey, doubleMatrixKey, EqCoord, eqCoordKey, floatArrayKey,
  floatKey, floatMatrixKey, intArrayKey,
  intKey, intMatrixKey,
  Key, longArrayKey,
  longKey, longMatrixKey, MinorPlanetCoord, minorPlanetCoordKey, shortArrayKey,
  shortKey, shortMatrixKey, SolarSystemCoord, solarSystemCoordKey,
  stringKey, SystemEvent, taiTimeKey, Units, utcTimeKey
} from "@tmtsoftware/esw-ts";

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

  static formatFloats(ar: Array<any>, units: Units | undefined): string {
    if (units == Units.hour)
      return ar.map(d => Angle.raToString(d * Angle.H2R, true)).join(', ')
    else if (units == Units.degree)
      return ar.map(d => Angle.deToString(d * Angle.D2R, true)).join(', ')
    else
      return ar.map(d => d.toFixed(6)).join(', ')
  }

  static formatArrays(ar: Array<any>): string {
    return ar.map(a => `[${a}]`).join(', ')
  }

  static formatFloatArrays(ar: Array<any>, units: Units | undefined): string {
    return ar.map(a => `[${this.formatFloats(a, units)}]`).join(', ')
  }

  static formatMatrixes(ar: Array<any>): string {
    return ar.map(a => `[${this.formatArrays(a)}]`).join(', ')
  }

  static formatFloatMatrices(ar: Array<any>, units: Units | undefined): string {
    return ar.map(a => `[${this.formatFloatArrays(a, units)}]`).join(', ')
  }

  static formatEqCoord(coord: EqCoord): string {
    const ra = Angle.raToString(coord.ra.toRadian())
    const dec = Angle.deToString(coord.dec.toRadian())
    const f = coord.frame.toString()
    const c = coord.catalogName
    const pm = `${coord.pm.pmx.toFixed(3)}, ${coord.pm.pmy.toFixed(3)}`
    return `tag: ${coord.tag}, ra: ${ra}, dec: ${dec}, frame: ${f}, catalog: ${c}, pm: ${pm}`
  }

  static formatEqCoords(ar: Array<any>): string {
    return ar.map(a => `[${this.formatEqCoord(a)}]`).join(', ')
  }

  // alt: Angle, az: Angle
  static formatAltAzCoord(coord: AltAzCoord): string {
    console.log(`XXX formatAltAzCoord `, coord)
    const alt = coord.alt.toDegree().toFixed(3)
    const az = coord.az.toDegree().toFixed(3)
    return `tag: ${coord.tag}, alt: ${alt}${Angle.DEGREE_SIGN}, az: ${az}${Angle.DEGREE_SIGN}`
  }

  static formatAltAzCoords(ar: Array<any>): string {
    return ar.map(a => `[${this.formatAltAzCoord(a)}]`).join(', ')
  }

  static formatSolarSystemCoord(coord: SolarSystemCoord): string {
    return `tag: ${coord.tag}, body: ${coord.body.toString()}`
  }

  static formatSolarSystemCoords(ar: Array<any>): string {
    return ar.map(a => `[${this.formatSolarSystemCoord(a)}]`).join(', ')
  }

  static formatMinorPlanetCoord(coord: MinorPlanetCoord): string {
    const epoch = coord.epoch.toFixed(1)
    const inclination = coord.inclination.toDegree().toFixed(3)
    const longAscendingNode = coord.longAscendingNode.toDegree().toFixed(3)
    const argOfPerihelion = coord.argOfPerihelion.toDegree().toFixed(3)
    const meanDistance = coord.meanDistance.toFixed(3)
    const eccentricity = coord.eccentricity.toFixed(3)
    const meanAnomaly = coord.meanAnomaly.toDegree().toFixed(3)
    return `tag: ${coord.tag}, epoch: ${epoch}, inclination: ${inclination}${Angle.DEGREE_SIGN}, longAscendingNode: ${longAscendingNode}${Angle.DEGREE_SIGN}, argOfPerihelion: ${argOfPerihelion}${Angle.DEGREE_SIGN}, meanDistance: ${meanDistance} AU, eccentricity: ${eccentricity}, meanAnomaly: ${meanAnomaly}${Angle.DEGREE_SIGN}`
  }

  static formatMinorPlanetCoords(ar: Array<any>): string {
    return ar.map(a => `[${this.formatMinorPlanetCoord(a)}]`).join(', ')
  }

  static formatCometCoord(coord: CometCoord): string {
    const epochOfPerihelion = coord.epochOfPerihelion.toFixed(1)
    const inclination = coord.inclination.toDegree().toFixed(3)
    const longAscendingNode = coord.longAscendingNode.toDegree().toFixed(3)
    const argOfPerihelion = coord.argOfPerihelion.toDegree().toFixed(3)
    const perihelionDistance = coord.perihelionDistance.toFixed(3)
    const eccentricity = coord.eccentricity.toFixed(3)
    return `tag: ${coord.tag}, epochOfPerihelion: ${epochOfPerihelion}, inclination: ${inclination}${Angle.DEGREE_SIGN}, longAscendingNode: ${longAscendingNode}${Angle.DEGREE_SIGN}, argOfPerihelion: ${argOfPerihelion}${Angle.DEGREE_SIGN}, perihelionDistance: ${perihelionDistance} AU, eccentricity: ${eccentricity}`
  }

  static formatCometCoords(ar: Array<any>): string {
    return ar.map(a => `[${this.formatCometCoord(a)}]`).join(', ')
  }

  // Note: Not supported
  // See https://tmt-project.atlassian.net/browse/CSW-145
  static formatCoords(ar: Array<any>): string {
    return ar.map(a => {
      switch (a._type) {
        case 'EqCoord':
          return `[${this.formatEqCoord(a)}]`
        case 'SolarSystemCoord':
          return `[${this.formatSolarSystemCoord(a)}]`
        case 'MinorPlanetCoord':
          return `[${this.formatMinorPlanetCoord(a)}]`
        case 'CometCoord':
          return `[${this.formatCometCoord(a)}]`
        case 'AltAzCoord':
          return `[${this.formatAltAzCoord(a)}]`
        default:
          return `[unknown Coord type ${a._type}]`
      }
    }).join(', ')
  }

  // Format the parameter values for the given key for display
  static formatValues(systemEvent: SystemEvent, cswParamKey: BaseKeyType<Key>): string {
    const param = systemEvent.get(cswParamKey)
    const units = param?.units
    const values = param?.values

    if (values && values.length > 0) {
      switch (cswParamKey.keyTag) {
        case 'IntKey':
          return values.join(', ')
        case 'LongKey':
          return values.join(', ')
        case 'ShortKey':
          return values.join(', ')
        case 'FloatKey':
          return this.formatFloats(values, units)
        case 'DoubleKey':
          return this.formatFloats(values, units)
        case 'ByteKey':
          return values.join(', ')
        case 'StringKey':
          return values.join(', ')
        case 'CharKey':
          return values.join(', ')
        case 'ChoiceKey':
          return values.join(', ')
        case 'IntMatrixKey':
          return this.formatMatrixes(values)
        case 'ByteMatrixKey':
          return this.formatMatrixes(values)
        case 'LongMatrixKey':
          return this.formatMatrixes(values)
        case 'ShortMatrixKey':
          return this.formatMatrixes(values)
        case 'FloatMatrixKey':
          return this.formatFloatMatrices(values, units)
        case 'DoubleMatrixKey':
          return this.formatFloatMatrices(values, units)
        case 'IntArrayKey':
          return this.formatArrays(values)
        case 'ByteArrayKey':
          return this.formatArrays(values)
        case 'LongArrayKey':
          return this.formatArrays(values)
        case 'ShortArrayKey':
          return this.formatArrays(values)
        case 'FloatArrayKey':
          return this.formatFloatArrays(values, units)
        case 'DoubleArrayKey':
          return this.formatFloatArrays(values, units)
        case 'BooleanKey':
          return values.join(', ')
        case 'UTCTimeKey':
          return values.join(', ')
        case 'TAITimeKey':
          return values.join(', ')
        case 'EqCoordKey':
          return this.formatEqCoords(values)
        case 'SolarSystemCoordKey':
          return this.formatSolarSystemCoords(values)
        case 'MinorPlanetCoordKey':
          return this.formatMinorPlanetCoords(values)
        case 'CometCoordKey':
          return this.formatCometCoords(values)
        case 'AltAzCoordKey':
          return this.formatAltAzCoords(values)
        case 'CoordKey':
          return this.formatCoords(values)
      }
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
