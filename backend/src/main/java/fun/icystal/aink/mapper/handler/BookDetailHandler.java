package fun.icystal.aink.mapper.handler;

import fun.icystal.aink.obj.entity.BookDetail;
import fun.icystal.aink.util.JsonUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(BookDetail.class)
public class BookDetailHandler extends BaseTypeHandler<BookDetail> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BookDetail parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtil.toJSONString(parameter));
    }

    @Override
    public BookDetail getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JsonUtil.parseObject(rs.getString(columnName), BookDetail.class);
    }

    @Override
    public BookDetail getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JsonUtil.parseObject(rs.getString(columnIndex), BookDetail.class);
    }

    @Override
    public BookDetail getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JsonUtil.parseObject(cs.getString(columnIndex), BookDetail.class);
    }
}
