package org.sample.helpers

object ClassHelper {
	/** without inheritable fields */
	def extractFieldNames(clazz: Class[_]): Seq[String] = {
		clazz.getDeclaredFields.filterNot(_.isSynthetic).map(_.getName)
	}

	/** without inheritable fields */
	def convertToTypeMap[T](obj: T) = {
		val fields = obj.getClass.getDeclaredFields.filterNot(_.isSynthetic)

		(Map[String, Any]() /: fields) { (result, field) =>
			field.setAccessible(true)
			result + (field.getName -> field.get(obj))
		}
	}

	def convertToFieldMap(obj: AnyRef) = {
		(Map[String, Any]() /: obj.getClass.getDeclaredFields) {(a, f) =>
			f.setAccessible(true)
			a + (f.getName -> f.get(obj))
		}
	}
}
