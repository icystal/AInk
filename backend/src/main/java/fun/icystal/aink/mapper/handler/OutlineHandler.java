package fun.icystal.aink.mapper.handler;

import fun.icystal.aink.obj.entity.outline.Outline;
import fun.icystal.aink.util.JsonUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(Outline.class)
public class OutlineHandler extends BaseTypeHandler<Outline> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Outline parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtil.toJSONString(parameter));
    }

    @Override
    public Outline getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JsonUtil.parseObject(rs.getString(columnName), Outline.class);
    }

    @Override
    public Outline getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JsonUtil.parseObject(rs.getString(columnIndex), Outline.class);
    }

    @Override
    public Outline getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JsonUtil.parseObject(cs.getString(columnIndex), Outline.class);
    }
}
