/**
 * Based on the CSW Angle class.
 *
 * An wrapper for angle. Normally angle would be stored in double
 * as radians, but this introduces rounding errors.
 * This class stores value in microarcseconds to prevent rounding errors.
 */
export class Angle {
  uas: number

  /** multiply to convert degrees to radians */
  static D2R: number = Math.PI / 180.0

  /** multiply to convert radians to degrees */
  static R2D: number = 1.0 / Angle.D2R

  /** multiply to convert degrees to archours */
  static D2H: number = 1.0 / 15.0

  /** multiply to convert archour to degrees */
  static H2D: number = 1.0 / Angle.D2H

  /** multiply to convert degrees to arcminute */
  static D2M: number = 60

  /** multiply to convert arcminute  to toDegree */
  static M2D: number = 1.0 / Angle.D2M

  /** multiply to convert degrees to arcsecond */
  static D2S: number = 3600

  /** multiply to convert arcsecond to toDegree */
  static S2D: number = 1.0 / Angle.D2S

  /** multiply to convert hours to radians */
  static H2R: number = Angle.H2D * Angle.D2R

  /** multiply to convert radians to hours */
  static R2H: number = 1.0 / Angle.H2R

  /** multiply to convert radians to minutes */
  static R2M: number = Angle.R2D * Angle.D2M

  /** multiply to convert minutes to radians */
  static M2R: number = 1.0 / Angle.R2M

  /** multiply to convert milliarcseconds to radians */
  static Mas2R: number = Angle.D2R / 3600000.0

  /** multiply to convert microarcseconds to radians */
  static Uas2R: number = Angle.D2R / 3600000000.0

  /** multiply to convert radians to milliarcseconds */
  static R2Mas: number = 1.0 / Angle.Mas2R

  /** multiply to convert radians to microarcseconds */
  static R2Uas: number = 1.0 / Angle.Uas2R

  /** multiply to convert hours to milliarcseconds */
  static H2Mas: number = 15 * 60 * 60 * 1000

  /** multiply to convert time minutes to milliarcseconds */
  static HMin2Mas: number = 15 * 60 * 1000

  /** multiply to convert time seconds to milliarcseconds */
  static HSec2Mas: number = 15 * 1000

  /** multiply to convert hours to microarcseconds */
  static H2Uas: number = 15 * 60 * 60 * 1000 * 1000

  /** multiply to convert time minutes to microarcseconds */
  static HMin2Uas: number = 15 * 60 * 1000 * 1000

  /** multiply to convert time seconds to microarcseconds */
  static HSec2Uas: number = 15 * 1000 * 1000

  /** multiply to convert degrees to milliarcseconds */
  static D2Mas: number = 60 * 60 * 1000

  /** multiply to convert minutes to milliarcseconds */
  static M2Mas: number = 60 * 1000

  /** multiply to convert Seconds to milliarcseconds */
  static S2Mas: number = 1000

  /** multiply to convert degrees to microarcseconds */
  static D2Uas: number = 60 * 60 * 1000 * 1000

  /** multiply to convert minutes to microarcseconds */
  static M2Uas: number = 60 * 1000 * 1000

  /** multiply to convert Seconds to microarcseconds */
  static S2Uas: number = 1000 * 1000

  /** multiply to convert UAS to degrees */
  static Uas2D: number = 1.0 / Angle.D2Uas

  /** multiply to convert UAS to minutes */
  static Uas2M: number = 1.0 / Angle.M2Uas

  /** multiply to convert UAS to Seconds */
  static Uas2S: number = 1.0 / Angle.S2Uas

  /** multiply to convert  arcseconds to radians */
  static S2R: number = Angle.D2R / 3600.0

  /** multiply to convert radians to  arcseconds */
  static R2S: number = 1.0 / Angle.S2R

  /** round circle which marks degrees */
  static DEGREE_SIGN = '\u00B0'

  constructor(uas: number) {
    this.uas = uas
  }

  static isNear(x: number, d: number): boolean {
    const tolerance = 1e-7
    return Math.abs(x % d) < tolerance || Math.abs(x % d - d) < tolerance
  }

  static formatSecs(sec: number): string {
    if (Angle.isNear(sec, 1))
      return `${Math.round(sec)}`
    else if (Angle.isNear(sec, 0.1))
      return `${sec.toFixed(1)}`
    else if (Angle.isNear(sec, 0.01))
      return `${sec.toFixed(2)}`
    else
      return `${sec.toFixed(3)}`
  }

  /**
   * convert RA to string in format '1h 2m'
   * minutes and seconds are auto added as needed
   *
   * @param ra in radians
   * @return ra in string form
   */
  static raToString(ra: number): string {
    if (Angle.isNear(ra, Angle.H2R)) {
      const hour = Math.round(ra * Angle.R2H)
      return `${hour}h`
    } else if (Angle.isNear(ra, Angle.H2R / 60.0)) {
      const hour = Math.trunc(ra * Angle.R2H)
      const min = Math.round((ra - Angle.H2R * hour) * Angle.R2H * 60)
      return `${hour}h ${min}m`
    } else {
      const hour = Math.trunc(ra * Angle.R2H)
      const min = Math.trunc((ra - Angle.H2R * hour) * Angle.R2H * 60)
      const sec = (ra - Angle.H2R * hour - min * Angle.H2R / 60) * Angle.R2H * 3600
      const s = Angle.formatSecs(sec)
      return `${hour}h ${min}m ${s}s`
    }
  }

  /**
   * convert DE to string in format '1d 2m'
   * minutes and seconds are auto added as needed
   *
   * @param de2 in radians
   * @return de in string form
   */
  static deToString(de2: number): string {
    const [de, sign] = (de2 < 0) ? [-de2, "-"] : [de2, ""]

    if (Angle.isNear(de, Angle.D2R)) {
      const deg = Math.trunc(Math.round(de * Angle.R2D))
      return sign + deg + Angle.DEGREE_SIGN
    } else if (Angle.isNear(de, Angle.M2R)) {
      const deg = Math.trunc(de * Angle.R2D)
      const min = Math.trunc((de - Angle.D2R * deg) * Angle.R2M)
      return sign + deg + Angle.DEGREE_SIGN + min + "'"
    } else {
      const deg = Math.trunc(de * Angle.R2D)
      const min = Math.trunc((de - Angle.D2R * deg) * Angle.R2D * 60)
      const sec = (de - Angle.D2R * deg - min * Angle.D2R / 60) * Angle.R2D * 3600
      const s = Angle.formatSecs(sec)
      return sign + deg + Angle.DEGREE_SIGN + min + "'" + s + "\""
    }
  }

  /** returns angle value in radians */
  toRadian(): number {
    return Angle.Uas2R * this.uas
  }

  /** returns angle value in degrees */
  toDegree(): number {
    return Angle.Uas2D * this.uas
  }

  /** returns angle value in milliarcseconds */
  toMas(): number  {
    return this.uas * 1e-3
  }

  /** returns angle value in arcseconds */
  toArcSec(): number  {
    return 1e-6 * this.uas
  }
}
