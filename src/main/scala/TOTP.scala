import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object TOTP:
  val DIGITS_POWER = (0 to 8).map("1" + "0" * _).map(_.toInt).toList
  def crypt(crypto: String, keyBytes: Array[Byte], text: Array[Byte]) =
    val key = new SecretKeySpec(keyBytes, "RAW")
    val mac = Mac.getInstance(crypto)
    mac.init(key)
    mac.doFinal(text)

  def hexStr2Bytes(hex: String) =
    BigInt("10" + hex, 16).toByteArray.drop(1)

  def gen(key: String, time: String, digits: Int, crypto: String) =
    val msg = hexStr2Bytes("0".repeat(16 - time.length) + time)
    val k = hexStr2Bytes(key)
    val hash = crypt(crypto, k, msg)
    val offset = hash(hash.length - 1) & 0xf

    val binary =
      ((hash(offset) & 0x7f) << 24) |
      ((hash(offset + 1) & 0xff) << 16) |
      ((hash(offset + 2) & 0xff) << 8) |
      (hash(offset + 3) & 0xff)

    val otp = (binary % DIGITS_POWER(digits)).toString
    val res = digits - otp.length
    if res > 0 then "0".repeat(res) + otp else otp
    
