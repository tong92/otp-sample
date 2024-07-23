import scodec.bits.Bases.Alphabets.Base32
object GoogleOTP:
  def qr(u: String, h: String, sec: String) = 
    // Base32.(sec)
    s"http://chart.apis.google.com/chart?cht=qr&amp;chs=300x300&amp;chl=otpauth://totp/${u}@${h}%%3Fsecret%%3D${}&amp;chld=H|0";
