function validateFormRegister(e) {
    // let noError=true;
    e.preventDefault();
    let password = $('#inputPassword')
    let validatePasswordShow = $('#validatePassword')
    let passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,15}$/;
    if (!passwordRegex.test(password)) {
        validatePasswordShow.html("Mật khẩu không đáp ứng yêu cầu: ít nhất 8 kí tự, có chữ hoa, chữ thường, kí tự đặc biệt và tối đa 15 kí tự.").css('color','red');
        return false;
    } else {
        $('#form-register').unbind('submit').submit();
    }
}